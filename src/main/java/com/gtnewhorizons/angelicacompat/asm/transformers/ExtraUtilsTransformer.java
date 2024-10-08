package com.gtnewhorizons.angelicacompat.asm.transformers;

import java.util.*;

import net.minecraft.launchwrapper.IClassTransformer;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;

import com.gtnewhorizons.angelicacompat.asm.AsmUtil;

public class ExtraUtilsTransformer implements IClassTransformer {

    private static final String RenderBlockColor = "com.rwtema.extrautils.block.render.RenderBlockColor";
    private static final String RenderBlockConnectedTextures = "com.rwtema.extrautils.block.render.RenderBlockConnectedTextures";
    private static final String RenderBlockConnectedTexturesEthereal = "com.rwtema.extrautils.block.render.RenderBlockConnectedTexturesEthereal";
    private static final String RenderBlockFullBright = "com.rwtema.extrautils.block.render.RenderBlockFullBright";
    private static final String RenderBlockSpike = "com.rwtema.extrautils.block.render.RenderBlockSpike";

    private static final List<String> transformedClasses = Arrays.asList(
        RenderBlockColor,
        RenderBlockConnectedTextures,
        RenderBlockConnectedTexturesEthereal,
        RenderBlockFullBright,
        RenderBlockSpike);

    private static final Map<String, Boolean> perThreadMap = new HashMap<>();

    static {
        perThreadMap.put(RenderBlockColor, false);
        perThreadMap.put(RenderBlockConnectedTextures, true);
        perThreadMap.put(RenderBlockConnectedTexturesEthereal, true);
        perThreadMap.put(RenderBlockFullBright, false);
        perThreadMap.put(RenderBlockSpike, false);
    }

    @Override
    public byte[] transform(final String className, String transformedName, byte[] basicClass) {
        if (basicClass == null) return null;

        if (!transformedClasses.contains(transformedName)) {
            return basicClass;
        }

        final ClassReader cr = new ClassReader(basicClass);
        final ClassNode cn = new ClassNode();
        cr.accept(cn, 0);

        AsmUtil.addThreadSafeISBRHAnnotation(transformedName, cn, perThreadMap.getOrDefault(transformedName, false));

        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        cn.accept(cw);
        return cw.toByteArray();
    }
}
