package com.example.repro

import org.jetbrains.kotlin.backend.common.extensions.IrGenerationExtension
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.builtins.StandardNames
import org.jetbrains.kotlin.ir.ObsoleteDescriptorBasedAPI
import org.jetbrains.kotlin.ir.declarations.IrModuleFragment
import org.jetbrains.kotlin.ir.types.IrSimpleType
import org.jetbrains.kotlin.ir.types.classifierOrFail
import org.jetbrains.kotlin.ir.types.typeOrNull
import org.jetbrains.kotlin.ir.util.getAnnotation
import org.jetbrains.kotlin.ir.util.render
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.resolve.descriptorUtil.fqNameSafe

class MyIrGenerationExtension : IrGenerationExtension {
    @OptIn(ObsoleteDescriptorBasedAPI::class)
    override fun generate(moduleFragment: IrModuleFragment, pluginContext: IrPluginContext) {
        val myAnnotatedClass = pluginContext.referenceClass(
            FqName("com.example.a.MyAnnotatedClass")
        )!!.owner

        val myAnnotation = myAnnotatedClass.getAnnotation(
            FqName("com.example.a.MyAnnotation")
        )

        val myAnnotationTypeArgument = (myAnnotation?.type as? IrSimpleType)
            ?.arguments?.singleOrNull()?.typeOrNull?.classifierOrFail?.descriptor?.fqNameSafe?.asString()

        check(myAnnotationTypeArgument == "kotlin.String") {
            "Expected 'kotlin.String' but was '$myAnnotationTypeArgument'"
        }
    }
}