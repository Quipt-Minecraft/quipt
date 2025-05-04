package com.quiptmc.fabric.blocks.abstracts;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.render.*;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import org.joml.Matrix4f;

import java.awt.*;

public abstract class QuiptBlockEntityRenderer<T extends BlockEntity> implements BlockEntityRenderer<T> {

    private final RenderLayer renderLayer;
    private final Color color;
    private final boolean renderInside;

    public QuiptBlockEntityRenderer(RenderLayer renderLayer, boolean renderInside, Color color) {
        this.renderLayer = renderLayer;
        this.renderInside = renderInside;
        this.color = color;
    }

    public QuiptBlockEntityRenderer(String key, boolean translucent, RenderPhase.ShaderProgram program, Identifier texture, boolean renderInside, Color color) {
        this.color = color;
        this.renderInside = renderInside;
        this.renderLayer = RenderLayer.of(key, VertexFormats.POSITION, VertexFormat.DrawMode.QUADS, 1536, false, translucent, RenderLayer.MultiPhaseParameters.builder().program(program).texture(RenderPhase.Textures.create().add(texture, false, false).build()).build(false));
    }

    @Override
    public void render(T entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        // Calculate animation time
        float animationTime = (entity.getWorld() != null) ? (entity.getWorld().getTime() + tickDelta) * 0.01F : 0;

        matrices.push();

        // Get appropriate vertex consumer for end portal effect
        VertexConsumer vertexConsumer = vertexConsumers.getBuffer(renderLayer);

        // Get current position matrix
        Matrix4f positionMatrix = matrices.peek().getPositionMatrix();

        this.renderBox(positionMatrix, vertexConsumer, animationTime);

        matrices.pop();
    }

    public void renderBox(Matrix4f matrix, VertexConsumer vertexConsumer, float animationTime) {
        // Draw all six faces of a cube
        this.renderCubeFace(matrix, vertexConsumer, 0, 1, 0, 1, 0, Direction.DOWN, animationTime);  // Bottom
        this.renderCubeFace(matrix, vertexConsumer, 0, 1, 0, 1, 1, Direction.UP, animationTime);    // Top
        this.renderCubeFace(matrix, vertexConsumer, 0, 1, 0, 1, 0, Direction.NORTH, animationTime); // North
        this.renderCubeFace(matrix, vertexConsumer, 0, 1, 0, 1, 1, Direction.SOUTH, animationTime); // South
        this.renderCubeFace(matrix, vertexConsumer, 0, 1, 0, 1, 0, Direction.WEST, animationTime);  // West
        this.renderCubeFace(matrix, vertexConsumer, 0, 1, 0, 1, 1, Direction.EAST, animationTime);  // East
    }

    public void renderCubeFace(Matrix4f matrix, VertexConsumer vertexConsumer, float minX, float maxX, float minY, float maxY, float depth, Direction direction, float animationTime) {
        // Color with appropriate end gateway hues
        float red = color.getRed() / 255f;
        float green = color.getGreen() / 255f;
        float blue = color.getBlue() / 255f;
        float alpha = color.getAlpha() / 255f;

        float u1 = 0.0f;
        float u2 = 1.0f;
        float v1 = 0.0f;
        float v2 = 1.0f;

        // Add animation based on world time
        u1 += animationTime * 0.3f;
        v1 += animationTime * 0.2f;

        switch (direction) {
            case DOWN:
                // Bottom face - Y is constant
                vertexConsumer.vertex(matrix, minX, depth, minY).color(red, green, blue, alpha).texture(u1, v1);
                vertexConsumer.vertex(matrix, maxX, depth, minY).color(red, green, blue, alpha).texture(u2, v1);
                vertexConsumer.vertex(matrix, maxX, depth, maxY).color(red, green, blue, alpha).texture(u2, v2);
                vertexConsumer.vertex(matrix, minX, depth, maxY).color(red, green, blue, alpha).texture(u1, v2);
                if (renderInside) {
                    vertexConsumer.vertex(matrix, minX, depth, minY).color(red, green, blue, alpha).texture(u1, v1);
                    vertexConsumer.vertex(matrix, minX, depth, maxY).color(red, green, blue, alpha).texture(u1, v2);
                    vertexConsumer.vertex(matrix, maxX, depth, maxY).color(red, green, blue, alpha).texture(u2, v2);
                    vertexConsumer.vertex(matrix, maxX, depth, minY).color(red, green, blue, alpha).texture(u2, v1);
                }
                break;
            case UP:
                // Top face - Y is constant
                vertexConsumer.vertex(matrix, minX, depth, minY).color(red, green, blue, alpha).texture(u1, v1);
                vertexConsumer.vertex(matrix, minX, depth, maxY).color(red, green, blue, alpha).texture(u1, v2);
                vertexConsumer.vertex(matrix, maxX, depth, maxY).color(red, green, blue, alpha).texture(u2, v2);
                vertexConsumer.vertex(matrix, maxX, depth, minY).color(red, green, blue, alpha).texture(u2, v1);
                if (renderInside) {
                    vertexConsumer.vertex(matrix, minX, depth, minY).color(red, green, blue, alpha).texture(u1, v1);
                    vertexConsumer.vertex(matrix, maxX, depth, minY).color(red, green, blue, alpha).texture(u2, v1);
                    vertexConsumer.vertex(matrix, maxX, depth, maxY).color(red, green, blue, alpha).texture(u2, v2);
                    vertexConsumer.vertex(matrix, minX, depth, maxY).color(red, green, blue, alpha).texture(u1, v2);
                }
                break;
            case NORTH:
                // North face - Z is constant
                vertexConsumer.vertex(matrix, minX, minY, depth).color(red, green, blue, alpha).texture(u1, v1);
                vertexConsumer.vertex(matrix, minX, maxY, depth).color(red, green, blue, alpha).texture(u1, v2);
                vertexConsumer.vertex(matrix, maxX, maxY, depth).color(red, green, blue, alpha).texture(u2, v2);
                vertexConsumer.vertex(matrix, maxX, minY, depth).color(red, green, blue, alpha).texture(u2, v1);
                if (renderInside) {
                    vertexConsumer.vertex(matrix, maxX, minY, depth).color(red, green, blue, alpha).texture(u2, v1);
                    vertexConsumer.vertex(matrix, maxX, maxY, depth).color(red, green, blue, alpha).texture(u2, v2);
                    vertexConsumer.vertex(matrix, minX, maxY, depth).color(red, green, blue, alpha).texture(u1, v2);
                    vertexConsumer.vertex(matrix, minX, minY, depth).color(red, green, blue, alpha).texture(u1, v1);
                }
                break;
            case SOUTH:
                // South face - Z is constant
                vertexConsumer.vertex(matrix, maxX, minY, depth).color(red, green, blue, alpha).texture(u2, v1);
                vertexConsumer.vertex(matrix, maxX, maxY, depth).color(red, green, blue, alpha).texture(u2, v2);
                vertexConsumer.vertex(matrix, minX, maxY, depth).color(red, green, blue, alpha).texture(u1, v2);
                vertexConsumer.vertex(matrix, minX, minY, depth).color(red, green, blue, alpha).texture(u1, v1);
                if (renderInside) {
                    vertexConsumer.vertex(matrix, minX, minY, depth).color(red, green, blue, alpha).texture(u1, v1);
                    vertexConsumer.vertex(matrix, minX, maxY, depth).color(red, green, blue, alpha).texture(u1, v2);
                    vertexConsumer.vertex(matrix, maxX, maxY, depth).color(red, green, blue, alpha).texture(u2, v2);
                    vertexConsumer.vertex(matrix, maxX, minY, depth).color(red, green, blue, alpha).texture(u2, v1);
                }
                break;
            case WEST:
                // West face - X is constant
                vertexConsumer.vertex(matrix, depth, minY, minY).color(red, green, blue, alpha).texture(u1, v1);
                vertexConsumer.vertex(matrix, depth, minY, maxY).color(red, green, blue, alpha).texture(u2, v1);
                vertexConsumer.vertex(matrix, depth, maxY, maxY).color(red, green, blue, alpha).texture(u2, v2);
                vertexConsumer.vertex(matrix, depth, maxY, minY).color(red, green, blue, alpha).texture(u1, v2);
                if (renderInside) {
                    vertexConsumer.vertex(matrix, depth, minY, minX).color(red, green, blue, alpha).texture(u1, v1);
                    vertexConsumer.vertex(matrix, depth, maxY, minX).color(red, green, blue, alpha).texture(u1, v2);
                    vertexConsumer.vertex(matrix, depth, maxY, maxX).color(red, green, blue, alpha).texture(u2, v2);
                    vertexConsumer.vertex(matrix, depth, minY, maxX).color(red, green, blue, alpha).texture(u2, v1);
                }
                break;
            case EAST:
                // East face - X is constant
                vertexConsumer.vertex(matrix, depth, minY, minX).color(red, green, blue, alpha).texture(u1, v1);
                vertexConsumer.vertex(matrix, depth, maxY, minX).color(red, green, blue, alpha).texture(u1, v2);
                vertexConsumer.vertex(matrix, depth, maxY, maxX).color(red, green, blue, alpha).texture(u2, v2);
                vertexConsumer.vertex(matrix, depth, minY, maxX).color(red, green, blue, alpha).texture(u2, v1);
                if (renderInside) {
                    vertexConsumer.vertex(matrix, depth, minY, minY).color(red, green, blue, alpha).texture(u1, v1);
                    vertexConsumer.vertex(matrix, depth, minY, maxY).color(red, green, blue, alpha).texture(u2, v1);
                    vertexConsumer.vertex(matrix, depth, maxY, maxY).color(red, green, blue, alpha).texture(u2, v2);
                    vertexConsumer.vertex(matrix, depth, maxY, minY).color(red, green, blue, alpha).texture(u1, v2);
                }
                break;
        }
    }
}
