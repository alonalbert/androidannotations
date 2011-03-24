/*
 * Copyright 2010-2011 Pierre-Yves Ricau (py.ricau at gmail.com)
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.googlecode.androidannotations.processing;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Map;

import javax.lang.model.element.Element;

import com.googlecode.androidannotations.annotations.Id;
import com.googlecode.androidannotations.generation.ValueInstruction;
import com.googlecode.androidannotations.model.AndroidRes;
import com.googlecode.androidannotations.model.Instruction;
import com.googlecode.androidannotations.model.MetaActivity;
import com.googlecode.androidannotations.model.MetaModel;
import com.googlecode.androidannotations.rclass.IRClass;
import com.googlecode.androidannotations.rclass.IRInnerClass;
import com.googlecode.androidannotations.rclass.RClass.Res;
import com.sun.codemodel.JCodeModel;

public class ResProcessor implements ElementProcessor {

	private final IRClass rClass;
	private final AndroidRes androidValue;

	public ResProcessor(AndroidRes androidValue, IRClass rClass) {
		this.rClass = rClass;
		this.androidValue = androidValue;
	}

	@Override
	public Class<? extends Annotation> getTarget() {
		return androidValue.getTarget();
	}

	@Override
	public void process(Element element, MetaModel metaModel) {

		String name = element.getSimpleName().toString();

		int idValue = androidValue.idFromElement(element);

		Res resInnerClass = androidValue.getRInnerClass();

		IRInnerClass rInnerClass = rClass.get(resInnerClass);
		String qualifiedId;
		if (idValue == Id.DEFAULT_VALUE) {
			String fieldName = element.getSimpleName().toString();
			qualifiedId = rInnerClass.getIdQualifiedName(fieldName);
		} else {
			qualifiedId = rInnerClass.getIdQualifiedName(idValue);
		}

		Element enclosingElement = element.getEnclosingElement();
		MetaActivity metaActivity = metaModel.getMetaActivities().get(enclosingElement);
		List<Instruction> beforeCreateInstructions = metaActivity.getBeforeCreateInstructions();

		Instruction instruction = new ValueInstruction(name, androidValue.getResourceMethodName(), qualifiedId);

		beforeCreateInstructions.add(instruction);

	}

	@Override
	public void process(Element element, JCodeModel codeModel, Map<Element, ActivityHolder> activityHolders) {
		// TODO Auto-generated method stub
		
	}

}
