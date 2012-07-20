/**
 *  Copyright 2011 Alexandru Craciun, Eyal Kaspi
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.stjs.generator.utils;

import static japa.parser.ast.body.ModifierSet.isStatic;
import japa.parser.ast.Node;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.body.Parameter;
import japa.parser.ast.type.ClassOrInterfaceType;
import japa.parser.ast.type.ReferenceType;
import japa.parser.ast.visitor.VoidVisitor;

import java.util.ArrayList;
import java.util.List;

import org.stjs.generator.visitor.ForEachNodeVisitor;

/**
 * different methods to work with AST nodes.
 * 
 * @author acraciun
 * 
 */
public class NodeUtils {
	public static boolean isMainMethod(MethodDeclaration methodDeclaration) {
		boolean isMainMethod = false;
		if (isStatic(methodDeclaration.getModifiers()) && "main".equals(methodDeclaration.getName())) {
			List<Parameter> parameters = methodDeclaration.getParameters();
			if ((parameters != null) && (parameters.size() == 1)) {
				Parameter parameter = parameters.get(0);
				if (parameter.getType() instanceof ReferenceType) {
					ReferenceType refType = (ReferenceType) parameter.getType();
					if ((refType.getArrayCount() == 1) && (refType.getType() instanceof ClassOrInterfaceType)) {
						String typeName = ((ClassOrInterfaceType) refType.getType()).getName();
						if ("String".equals(typeName) || "java.lang.String".equals(typeName)) {
							isMainMethod = true;
						}
					}
				}
			}
		}
		return isMainMethod;
	}

	/**
	 * 
	 * @param parent
	 * @param type
	 * @return the list of all the descendants of the given code that are of the given type (or a subclass of it)
	 */
	public static final <T extends Node> List<T> findDescendantsOfType(Node parent, final Class<T> type) {
		final List<T> children = new ArrayList<T>();
		VoidVisitor<Boolean> visitor = new ForEachNodeVisitor<Boolean>() {
			@SuppressWarnings("unchecked")
			@Override
			protected void before(Node node, Boolean arg) {
				if (type.isAssignableFrom(node.getClass())) {
					children.add((T) node);
				}
			}
		};
		parent.accept(visitor, null);
		return children;
	}
}
