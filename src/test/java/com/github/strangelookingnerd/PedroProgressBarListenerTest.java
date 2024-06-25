/*
 * The MIT License
 *
 * Copyright (c) 2024 strangelookingnerd
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

import com.intellij.ide.ui.laf.LafManagerImpl;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.IdeFrame;
import com.intellij.openapi.wm.StatusBar;
import com.intellij.ui.BalloonLayout;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.Test;

import javax.swing.JComponent;
import javax.swing.UIManager;
import javax.swing.plaf.metal.MetalProgressBarUI;
import java.awt.Rectangle;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class PedroProgressBarListenerTest {

    @Test
    void testUpdateProgressBar() {
        // defaults
        assertEquals(MetalProgressBarUI.class.getName(), UIManager.get("ProgressBarUI"));
        assertNull(UIManager.getDefaults().get(PedroProgressBarUI.class.getName()));

        // ctor.
        PedroProgressBarListener listener = new PedroProgressBarListener();
        assertEquals(PedroProgressBarUI.class.getName(), UIManager.get("ProgressBarUI"));
        assertEquals(PedroProgressBarUI.class, UIManager.getDefaults().get(PedroProgressBarUI.class.getName()));

        // reset
        UIManager.put("ProgressBarUI", null);
        UIManager.getDefaults().remove(PedroProgressBarUI.class.getName());
        assertEquals(MetalProgressBarUI.class.getName(), UIManager.get("ProgressBarUI"));
        assertNull(UIManager.getDefaults().get(PedroProgressBarUI.class.getName()));

        // applicationActivated
        listener.applicationActivated(new TestFrame());
        assertEquals(PedroProgressBarUI.class.getName(), UIManager.get("ProgressBarUI"));
        assertEquals(PedroProgressBarUI.class, UIManager.getDefaults().get(PedroProgressBarUI.class.getName()));

        // reset
        UIManager.put("ProgressBarUI", null);
        UIManager.getDefaults().remove(PedroProgressBarUI.class.getName());
        assertEquals(MetalProgressBarUI.class.getName(), UIManager.get("ProgressBarUI"));
        assertNull(UIManager.getDefaults().get(PedroProgressBarUI.class.getName()));

        // lookAndFeelChanged
        listener.lookAndFeelChanged(new LafManagerImpl());
        assertEquals(PedroProgressBarUI.class.getName(), UIManager.get("ProgressBarUI"));
        assertEquals(PedroProgressBarUI.class, UIManager.getDefaults().get(PedroProgressBarUI.class.getName()));
    }

    private static class TestFrame implements IdeFrame {

        @Override
        public @Nullable StatusBar getStatusBar() {
            return null;
        }

        @Override
        public @NotNull Rectangle suggestChildFrameBounds() {
            return new Rectangle(0, 0, 100, 100);
        }

        @Override
        public @Nullable Project getProject() {
            return null;
        }

        @Override
        public void setFrameTitle(String title) {
            // NOP
        }

        @Override
        public JComponent getComponent() {
            return null;
        }

        @Override
        public @Nullable BalloonLayout getBalloonLayout() {
            return null;
        }
    }

}