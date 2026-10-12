# Minecraft Server Shutdown Fix - Summary

## Problem
The Fabric server was unable to stop gracefully and required manual killing of the process. This is a common issue when threads or executor services aren't properly shut down.

## Root Cause Analysis
The issue was caused by multiple factors:

1. **Infinite Task Scheduler Loop**: The `TaskScheduler` class uses a `ScheduledExecutorService` that was never properly shut down. The `Heartbeat` class was continuously rescheduling itself via `TaskScheduler.scheduleAsyncTask(this, 0, TimeUnit.SECONDS)`, creating an infinite loop of tasks.

2. **No Shutdown Hook for Fabric**: While the embedded Jetty server and other resources had shutdown logic, there was no listener registered to trigger graceful shutdown when the Minecraft server stops.

3. **Incomplete Shutdown Logic**: The `QuiptIntegration` class lacked proper shutdown methods to stop the heartbeat and task scheduler.

## Changes Made

### 1. Enhanced TaskScheduler (`modules/core/src/main/java/live/qsmc/quipt/core/utils/TaskScheduler.java`)
- Added `isShuttingDown` flag to prevent new tasks from being scheduled during shutdown
- Improved `shutdown()` method with:
  - Graceful shutdown attempt with 5-second timeout
  - Forceful shutdown (`shutdownNow()`) if tasks don't complete in time
  - Additional 2-second wait for forced shutdown to complete
  - Proper exception handling for interrupts
- Added `isShuttingDown()` method to check shutdown state

### 2. Updated Heartbeat (`modules/core/src/main/java/live/qsmc/quipt/core/heartbeat/Heartbeat.java`)
- Added `isShuttingDown` flag
- Added `shutdown()` method to gracefully stop the heartbeat
- Modified `run()` method to check if scheduler is shutting down before rescheduling
- Clears all flutters when shutdown is initiated

### 3. Enhanced QuiptIntegration (`modules/core/src/main/java/live/qsmc/quipt/core/QuiptIntegration.java`)
- Added `isShuttingDown` flag to track shutdown state
- Added `shutdown()` method that:
  - Prevents multiple shutdown calls
  - Shuts down the heartbeat
  - Shuts down the task scheduler
  - Logs all shutdown steps
- Added `isShuttingDown()` method to check if shutdown is in progress
- Imported `TaskScheduler` for shutdown coordination

### 4. Created FabricLifecycleListener (`modules/fabric/src/main/java/live/qsmc/quipt/fabric/listener/FabricLifecycleListener.java`)
- New listener class implementing `ServerLifecycleEvents.ServerStopping`
- Listens for server shutdown events
- Triggers graceful shutdown of both the Fabric integration and core Quipt instance
- Includes error handling to prevent shutdown errors from affecting server stop

### 5. Updated QuiptFabric (`modules/fabric/src/main/java/live/qsmc/quipt/fabric/QuiptFabric.java`)
- Registered the `FabricLifecycleListener` during initialization
- Added import for `ServerLifecycleEvents`
- Ensures the listener is registered to handle server shutdown

## How It Works

1. When the Minecraft server receives a stop command, Fabric triggers `ServerLifecycleEvents.SERVER_STOPPING`
2. `FabricLifecycleListener.onServerStopping()` is called
3. It triggers `QuiptIntegration.shutdown()` on both the Fabric integration and core Quipt instance
4. `QuiptIntegration.shutdown()`:
   - Sets `isShuttingDown` flag to prevent new operations
   - Calls `Heartbeat.shutdown()` to stop all scheduled flutters
   - Calls `TaskScheduler.shutdown()` to gracefully shut down the executor service
5. The executor service:
   - Stops accepting new tasks
   - Waits up to 5 seconds for existing tasks to complete
   - Forces shutdown if timeout is exceeded
   - Waits up to 2 more seconds for forced shutdown

## Result
The server should now shut down cleanly without requiring manual process killing. The shutdown sequence ensures:
- No new background tasks are scheduled during shutdown
- Existing tasks have time to complete gracefully
- Threads are forcefully terminated if they don't shut down in time
- All resources are properly cleaned up

## Testing
To verify the fix works:
1. Start the Fabric server with Quipt installed
2. Run `/stop` command from console
3. Observe that the server shuts down without hanging
4. Check logs for shutdown messages from Quipt components

