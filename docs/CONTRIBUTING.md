# Contributing to QUIPT

Thank you for your interest in contributing! This guide explains how to set up your environment, propose changes, and follow project conventions.

Development Environment
- Requirements: Java 21, Git, and a modern IDE (IntelliJ IDEA recommended).
- Clone: git clone https://github.com/Quipt-Minecraft/quipt.git
- Build: On Windows PowerShell use .\gradlew.bat build, otherwise ./gradlew build
- Run Paper locally: Use a test Paper server and place the paper module JAR into plugins/.

Project Conventions
- Keep platform-agnostic logic in core/common modules.
- Paper/Fabric modules should only contain platform-specific wiring and code.
- Prefer small, focused PRs.
- Write clear commit messages and PR descriptions.

Coding Style
- Java code style consistent with standard conventions (Google/IntelliJ defaults are acceptable).
- Use descriptive names and Javadoc for public APIs and important classes.

Testing
- Add tests where feasible. For integration-style features (web handlers, Discord), add logs or mockable boundaries.
- Ensure builds pass locally before opening a PR.

Opening a Pull Request
- Fork the repository and create a feature branch.
- Update documentation (README/docs) if behavior or APIs change.
- Link related issues in the PR description.
- Be responsive to feedback during review.

Issue Reporting
- Use GitHub Issues. Include:
  - Environment details (Minecraft, Java version, platform)
  - Steps to reproduce
  - Expected vs actual behavior
  - Relevant logs or stack traces

Community
- Join our Discord for discussion and support: https://discord.gg/EhfMJmjTXh

License
- By contributing, you agree that your contributions will be licensed under the project’s license.
