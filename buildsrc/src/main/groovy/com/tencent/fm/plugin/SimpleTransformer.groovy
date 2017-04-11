package com.tencent.fm.plugin

import com.android.build.api.transform.*
import javassist.ClassPool
import javassist.CtClass
import javassist.CtMethod
import org.gradle.api.Project
import org.apache.commons.io.FileUtils;

public class SimpleTransformer extends Transform {

    Project mProject;
    AssistExtension ext;


    SimpleTransformer(Project mProject) {
        this.mProject = mProject
    }

    SimpleTransformer(Project mProject, AssistExtension extension) {
        this.mProject = mProject
        this.ext = extension
    }

    @Override
    public String getName() {
        return "InjectPlugin";
    }

    @Override
    public Set<QualifiedContent.ContentType> getInputTypes() {
        return Collections.singleton(QualifiedContent.DefaultContentType.CLASSES);
    }

    @Override
    public Set<QualifiedContent.Scope> getScopes() {
        return Collections.singleton(QualifiedContent.Scope.PROJECT);
    }

    @Override
    public boolean isIncremental() {
        return false;
    }

    @Override
    public void transform(Context context, Collection<TransformInput> inputs, Collection<TransformInput> referencedInputs, TransformOutputProvider outputProvider, boolean isIncremental) throws IOException, TransformException, InterruptedException {
        File outDir = outputProvider.getContentLocation("InjectPlugin", outputTypes, scopes, Format.DIRECTORY);
        outDir.deleteDir()
        outDir.mkdirs()
        System.out.println("output provider is " + outDir.absolutePath);

       // def insertJava = "System.out.println(\"hello world\");";
       // def activityName = "com.sngfm.www.data_binding_example.LoginActivity"
        def activityName = ext.className;
        def insertJava = ext.insertJavaStr;
        def methodName = ext.methodName; //onCreate
        def descriptor = ext.methodDescriptor; //(Landroid/os/Bundle);V

        ClassPool classPool = ClassPool.getDefault();
        classPool.appendSystemPath()
        def sdkVersion = mProject.android.compileSdkVersion;
        def androidjar = mProject.android.getSdkDirectory().getAbsolutePath() + "/platforms/" + sdkVersion + "/android.jar"
        classPool.appendClassPath(androidjar);

        inputs.each {
            it.directoryInputs.each {
                def dirPath = it.file.absolutePath;
                classPool.appendClassPath(dirPath)

                CtClass ctClass = classPool.getCtClass(activityName);
                if (ctClass.isFrozen()) {
                    ctClass.defrost();
                }
                CtMethod method = ctClass.getMethod(methodName, descriptor)
                method.insertBefore(insertJava);

                ctClass.writeFile(dirPath);
                ctClass.detach();

                def dest = outputProvider.getContentLocation(getName(),
                        getInputTypes(), getScopes(),
                        Format.DIRECTORY)
                FileUtils.copyDirectory(it.file, dest)
            }
        }

    }
}