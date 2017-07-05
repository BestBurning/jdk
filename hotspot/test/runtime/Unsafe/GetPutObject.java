/*
 * Copyright (c) 2015, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

/*
 * @test
 * @summary Verify behaviour of Unsafe.get/putObject
 * @library /testlibrary
 * @modules java.base/jdk.internal.misc
 *          java.management
 * @run main GetPutObject
 */

import java.lang.reflect.Field;
import jdk.test.lib.*;
import sun.misc.Unsafe;
import static jdk.test.lib.Asserts.*;

public class GetPutObject {
    public static void main(String args[]) throws Exception {
        Unsafe unsafe = Utils.getUnsafe();
        Test t = new Test();
        Object o = new Object();
        Field field = Test.class.getField("o");

        long offset = unsafe.objectFieldOffset(field);
        assertEquals(t.o, unsafe.getObject(t, offset));

        unsafe.putObject(t, offset, o);
        assertEquals(o, unsafe.getObject(t, offset));

        Object arrayObject[] = { unsafe, null, new Object() };
        int scale = unsafe.arrayIndexScale(arrayObject.getClass());
        offset = unsafe.arrayBaseOffset(arrayObject.getClass());
        for (int i = 0; i < arrayObject.length; i++) {
            assertEquals(unsafe.getObject(arrayObject, offset), arrayObject[i]);
            offset += scale;
        }
    }

    static class Test {
        public Object o = new Object();
    }
}
