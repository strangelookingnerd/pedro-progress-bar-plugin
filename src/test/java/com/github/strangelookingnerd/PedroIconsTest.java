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

import com.intellij.ui.scale.JBUIScale;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import javax.swing.ImageIcon;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PedroIconsTest {

    private static final Pattern DESCRIPTION_PATTERN = Pattern.compile("^Pedro (\\d\\d)x(\\d\\d)$");

    @ParameterizedTest
    @ValueSource(floats = {0.75F, 1F, 1.1F, 1.25F, 1.5F, 1.75F, 2F, 3F})
    void testScaling(float scaleFactor) {
        JBUIScale.setUserScaleFactorForTest(scaleFactor);

        ImageIcon icon = PedroIcons.getScaledIcon();
        assertNotNull(icon);

        if (scaleFactor < 1F || scaleFactor > 2F) {
            assertNull(icon.getImage());
            assertNull(icon.getDescription());
        } else {
            assertNotNull(icon.getImage());

            Matcher matcher = DESCRIPTION_PATTERN.matcher(icon.getDescription());
            assertTrue(matcher.matches());
            assertEquals(Integer.valueOf(matcher.group(1)), icon.getIconWidth());
            assertEquals(Integer.valueOf(matcher.group(2)), icon.getIconHeight());
        }
    }
}