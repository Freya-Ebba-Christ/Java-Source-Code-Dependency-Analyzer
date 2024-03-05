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
import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class DependencyAnalyzer {
    private final HashMap<String, Set<String>> dependencies = new HashMap<>();

    public void analyzeFile(String filePath) {
        try (FileInputStream fis = new FileInputStream(filePath)) {
            JavaParser javaParser = new JavaParser();
            ParseResult<CompilationUnit> parseResult = javaParser.parse(fis);

            if (parseResult.isSuccessful() && parseResult.getResult().isPresent()) {
                CompilationUnit cu = parseResult.getResult().get();

                cu.accept(new VoidVisitorAdapter<Void>() {
                    @Override
                    public void visit(ClassOrInterfaceDeclaration n, Void arg) {
                        super.visit(n, arg);
                        String className = n.getNameAsString();
                        dependencies.putIfAbsent(className, new HashSet<>());

                        n.getExtendedTypes().forEach(t -> dependencies.get(className).add(t.getNameAsString()));
                        n.getImplementedTypes().forEach(t -> dependencies.get(className).add(t.getNameAsString()));
                    }
                }, null);
            }
        } catch (Exception e) {
        }
    }

    public HashMap<String, Set<String>> getDependencies() {
        return dependencies;
    }
}

