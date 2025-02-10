/*
 * The MIT License
 *
 * Copyright (c) 2025 strangelookingnerd
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.github.strangelookingnerd;

import com.intellij.openapi.progress.util.ProgressBarUtil;
import com.intellij.ui.scale.JBUIScale;
import com.intellij.util.ui.JBInsets;
import com.intellij.util.ui.JBUI;
import com.intellij.util.ui.UIUtilities;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicProgressBarUI;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;

/**
 * The actual progress bar implementation. Heavily inspired by {@link com.intellij.ide.ui.laf.darcula.ui.DarculaProgressBarUI}
 */
public class PedroProgressBarUI extends BasicProgressBarUI {

    private static final int CYCLE_TIME_DEFAULT = 800;
    private static final int REPAINT_INTERVAL_DEFAULT = 50;

    private static final int DEFAULT_STRIPE_WIDTH = 4;
    private static final int DEFAULT_SCALING_BASE_LINE = 16;

    @SuppressWarnings({"MethodOverridesStaticMethodOfSuperclass", "UnusedDeclaration"})
    public static ComponentUI createUI(JComponent component) {
        return new PedroProgressBarUI();
    }

    @Override
    protected void installDefaults() {
        super.installDefaults();
        UIManager.put("ProgressBar.repaintInterval", REPAINT_INTERVAL_DEFAULT);
        UIManager.put("ProgressBar.cycleTime", CYCLE_TIME_DEFAULT);
    }

    @Override
    protected void paintIndeterminate(Graphics graphics, JComponent component) {
        Graphics2D graphics2D = (Graphics2D) graphics.create();
        try {
            graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            graphics2D.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_NORMALIZE);

            Rectangle r = new Rectangle(progressBar.getSize());
            if (component.isOpaque() && component.getParent() != null) {
                graphics2D.setColor(component.getParent().getBackground());
                graphics2D.fill(r);
            }

            Insets i = progressBar.getInsets();
            JBInsets.removeFrom(r, i);
            int orientation = progressBar.getOrientation();

            Color startColor;
            Color endColor;
            Color foreground = progressBar.getForeground();
            Object statusProperty = progressBar.getClientProperty(ProgressBarUtil.STATUS_KEY);
            if (ProgressBarUtil.FAILED_VALUE.equals(statusProperty) || foreground == JBUI.CurrentTheme.ProgressBar.FAILED) {
                startColor = JBUI.CurrentTheme.ProgressBar.FAILED;
                endColor = JBUI.CurrentTheme.ProgressBar.FAILED_END;
            } else if (ProgressBarUtil.PASSED_VALUE.equals(statusProperty) || foreground == JBUI.CurrentTheme.ProgressBar.PASSED) {
                startColor = JBUI.CurrentTheme.ProgressBar.PASSED;
                endColor = JBUI.CurrentTheme.ProgressBar.PASSED_END;
            } else if (ProgressBarUtil.WARNING_VALUE.equals(statusProperty) || foreground == JBUI.CurrentTheme.ProgressBar.WARNING) {
                startColor = JBUI.CurrentTheme.ProgressBar.WARNING;
                endColor = JBUI.CurrentTheme.ProgressBar.WARNING_END;
            } else {
                startColor = progressBar.getClientProperty("ProgressBar.indeterminateStartColor") instanceof Color color ?
                        color : JBUI.CurrentTheme.ProgressBar.INDETERMINATE_START;
                endColor = progressBar.getClientProperty("ProgressBar.indeterminateEndColor") instanceof Color color ?
                        color : JBUI.CurrentTheme.ProgressBar.INDETERMINATE_END;
            }

            Shape shape;
            int step = JBUIScale.scale(6);
            if (orientation == SwingConstants.HORIZONTAL) {
                int pHeight = getStripeWidth();
                int yOffset = r.y + (r.height - pHeight) / 2;

                shape = getShapedRect(r.x, yOffset, r.width, pHeight, pHeight);
                graphics2D.setPaint(new GradientPaint(r.x + getAnimationIndex() * step * 2F, yOffset, startColor,
                        r.x + getFrameCount() * step + getAnimationIndex() * step * 2F, yOffset, endColor, true));
            } else {
                int pWidth = getStripeWidth();
                int xOffset = r.x + (r.width - pWidth) / 2;

                shape = getShapedRect(xOffset, r.y, pWidth, r.height, pWidth);
                graphics2D.setPaint(new GradientPaint(xOffset, r.y + getAnimationIndex() * step * 2F, startColor,
                        xOffset, r.y + getFrameCount() * step + getAnimationIndex() * step * 2F, endColor, true));
            }
            graphics2D.fill(shape);

            ImageIcon icon = PedroIcons.getScaledIcon();
            if (orientation == SwingConstants.HORIZONTAL) {
                icon.paintIcon(progressBar, graphics2D, r.x + getAnimationIndex() * step * 2, r.y);
            } else {
                icon.paintIcon(progressBar, graphics2D, r.x, r.y + getAnimationIndex() * step * 2);
            }

            // Paint text
            if (progressBar.isStringPainted()) {
                if (progressBar.getOrientation() == SwingConstants.HORIZONTAL) {
                    paintString((Graphics2D) graphics, i.left, i.top, r.width, r.height, boxRect.x, boxRect.width);
                } else {
                    paintString((Graphics2D) graphics, i.left, i.top, r.width, r.height, boxRect.y, boxRect.height);
                }
            }
        } finally {
            graphics2D.dispose();
        }
    }

    @Override
    protected void paintDeterminate(Graphics graphics, JComponent component) {
        Graphics2D graphics2D = (Graphics2D) graphics.create();
        try {
            graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            graphics2D.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_NORMALIZE);

            Rectangle r = new Rectangle(progressBar.getSize());
            if (component.isOpaque() && component.getParent() != null) {
                graphics2D.setColor(component.getParent().getBackground());
                graphics2D.fill(r);
            }

            Insets i = progressBar.getInsets();
            JBInsets.removeFrom(r, i);
            int amountFull = getAmountFull(i, r.width, r.height);

            Shape fullShape;
            Shape coloredShape;
            int orientation = progressBar.getOrientation();
            if (orientation == SwingConstants.HORIZONTAL) {
                int pHeight = getStripeWidth();
                int yOffset = r.y + (r.height - pHeight) / 2;

                fullShape = getShapedRect(r.x, yOffset, r.width, pHeight, pHeight);
                coloredShape = getShapedRect(r.x, yOffset, amountFull, pHeight, pHeight);
            } else {
                int pWidth = getStripeWidth();
                int xOffset = r.x + (r.width - pWidth) / 2;

                fullShape = getShapedRect(xOffset, r.y, pWidth, r.height, pWidth);
                coloredShape = getShapedRect(xOffset, r.y, pWidth, amountFull, pWidth);
            }
            graphics2D.setColor(JBUI.CurrentTheme.ProgressBar.TRACK);
            graphics2D.fill(fullShape);

            Color foreground = progressBar.getForeground();
            Object statusProperty = progressBar.getClientProperty(ProgressBarUtil.STATUS_KEY);
            if (ProgressBarUtil.FAILED_VALUE.equals(statusProperty) || foreground == JBUI.CurrentTheme.ProgressBar.FAILED) {
                graphics.setColor(JBUI.CurrentTheme.ProgressBar.FAILED);
            } else if (ProgressBarUtil.PASSED_VALUE.equals(statusProperty) || foreground == JBUI.CurrentTheme.ProgressBar.PASSED) {
                graphics.setColor(JBUI.CurrentTheme.ProgressBar.PASSED);
            } else if (ProgressBarUtil.WARNING_VALUE.equals(statusProperty) || foreground == JBUI.CurrentTheme.ProgressBar.WARNING) {
                graphics.setColor(JBUI.CurrentTheme.ProgressBar.WARNING);
            } else {
                graphics2D.setColor(JBUI.CurrentTheme.ProgressBar.PROGRESS);
            }
            graphics2D.fill(coloredShape);

            ImageIcon icon = PedroIcons.getScaledIcon();
            if (orientation == SwingConstants.HORIZONTAL) {
                icon.paintIcon(progressBar, graphics2D, amountFull, r.y);
            } else {
                icon.paintIcon(progressBar, graphics2D, r.x, amountFull);
            }

            if (progressBar.isStringPainted()) {
                paintString(graphics, i.left, i.top, r.width, r.height, amountFull, i);
            }
        } finally {
            graphics2D.dispose();
        }
    }

    @Override
    public Dimension getPreferredSize(JComponent component) {
        Dimension size = super.getPreferredSize(component);
        if (!(component instanceof JProgressBar)) {
            return size;
        }
        if (!((JProgressBar) component).isStringPainted()) {
            if (((JProgressBar) component).getOrientation() == SwingConstants.HORIZONTAL) {
                size.height = JBUIScale.scale(DEFAULT_SCALING_BASE_LINE);
            } else {
                size.width = JBUIScale.scale(DEFAULT_SCALING_BASE_LINE);
            }
        }
        return size;
    }

    @Override
    protected int getBoxLength(int availableLength, int otherDimension) {
        return availableLength;
    }

    private Shape getShapedRect(float x, float y, float w, float h, float ar) {
        return progressBar.getClientProperty("ProgressBar.flatEnds") == Boolean.TRUE
                ? new Rectangle2D.Float(x, y, w, h)
                : new RoundRectangle2D.Float(x, y, w, h, ar, ar);
    }

    private int getStripeWidth() {
        Object property = progressBar.getClientProperty("ProgressBar.stripeWidth");
        if (property != null) {
            try {
                return JBUIScale.scale(Integer.parseInt(property.toString()));
            } catch (NumberFormatException nfe) {
                return JBUIScale.scale(DEFAULT_STRIPE_WIDTH);
            }
        } else {
            return JBUIScale.scale(DEFAULT_STRIPE_WIDTH);
        }
    }

    private void paintString(Graphics2D graphics2D, int x, int y, int w, int h, int fillStart, int amountFull) {
        String progressString = progressBar.getString();
        graphics2D.setFont(progressBar.getFont());
        Point renderLocation = getStringPlacement(graphics2D, progressString, x, y, w, h);
        Rectangle oldClip = graphics2D.getClipBounds();

        if (progressBar.getOrientation() == SwingConstants.HORIZONTAL) {
            graphics2D.setColor(getSelectionBackground());
            UIUtilities.drawString(progressBar, graphics2D, progressString, renderLocation.x, renderLocation.y);

            graphics2D.setColor(getSelectionForeground());
            graphics2D.clipRect(fillStart, y, amountFull, h);
            UIUtilities.drawString(progressBar, graphics2D, progressString, renderLocation.x, renderLocation.y);
        } else {
            graphics2D.setColor(getSelectionBackground());
            AffineTransform rotate = AffineTransform.getRotateInstance(Math.PI / 2);
            graphics2D.setFont(progressBar.getFont().deriveFont(rotate));
            renderLocation = getStringPlacement(graphics2D, progressString, x, y, w, h);
            UIUtilities.drawString(progressBar, graphics2D, progressString, renderLocation.x, renderLocation.y);

            graphics2D.setColor(getSelectionForeground());
            graphics2D.clipRect(x, fillStart, w, amountFull);
            UIUtilities.drawString(progressBar, graphics2D, progressString, renderLocation.x, renderLocation.y);
        }
        graphics2D.setClip(oldClip);
    }
}