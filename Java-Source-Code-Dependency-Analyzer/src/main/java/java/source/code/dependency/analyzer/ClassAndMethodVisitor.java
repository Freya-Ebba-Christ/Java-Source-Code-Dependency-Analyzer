package java.source.code.dependency.analyzer;

/*
 * Application.java
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 * @author Freya Ebba Christ 
 */
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ClassAndMethodVisitor extends VoidVisitorAdapter<Void> {
    private final Map<String, Set<String>> methodCallGraph;

    public ClassAndMethodVisitor(Map<String, Set<String>> methodCallGraph) {
        this.methodCallGraph = methodCallGraph;
    }

    @Override
    public void visit(ClassOrInterfaceDeclaration n, Void arg) {
        super.visit(n, arg);
        n.getMethods().forEach(method -> {
            String className = n.getFullyQualifiedName().orElse("[Anonymous]");
            String methodSignature = className + "." + method.getSignature().asString();
            methodCallGraph.putIfAbsent(methodSignature, new HashSet<>());
            method.accept(new MethodCallVisitor(methodCallGraph), methodSignature);
        });
    }
}
