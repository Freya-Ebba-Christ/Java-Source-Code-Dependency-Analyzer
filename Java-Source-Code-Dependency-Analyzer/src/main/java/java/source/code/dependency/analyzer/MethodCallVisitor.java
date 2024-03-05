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
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.resolution.declarations.ResolvedMethodDeclaration;
import java.util.Map;
import java.util.Set;

public class MethodCallVisitor extends VoidVisitorAdapter<String> {
    private Map<String, Set<String>> methodCallGraph;

    public MethodCallVisitor(Map<String, Set<String>> methodCallGraph) {
        this.methodCallGraph = methodCallGraph;
    }

    @Override
    public void visit(MethodCallExpr m, String currentMethodSignature) {
        super.visit(m, currentMethodSignature);
        try {
            ResolvedMethodDeclaration resolved = m.resolve();
            String calleeSignature = resolved.getQualifiedSignature();
            methodCallGraph.get(currentMethodSignature).add(calleeSignature);
        } catch (Exception e) {
            // Handle resolution error
        }
    }
}
