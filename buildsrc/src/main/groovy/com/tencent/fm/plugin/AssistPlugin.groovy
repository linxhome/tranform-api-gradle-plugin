package com.tencent.fm.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project

public class AssistPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        project.extensions.add("java_assist", AssistExtension)
        AssistExtension ext = project.extensions.getByName("java_assist")
        project.android.registerTransform(new SimpleTransformer(project,ext));
    }
}