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
import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.symbolsolver.JavaSymbolSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class DependencyAnalyzer {
    private final Map<String, Set<String>> methodCallGraph = new HashMap<>();
    private final JavaParser javaParser;

    public DependencyAnalyzer() {
        CombinedTypeSolver combinedTypeSolver = new CombinedTypeSolver(new ReflectionTypeSolver());
        JavaSymbolSolver symbolSolver = new JavaSymbolSolver(combinedTypeSolver);

        ParserConfiguration configuration = new ParserConfiguration();
        configuration.setSymbolResolver(symbolSolver);
        
        this.javaParser = new JavaParser(configuration);
    }

    public void analyzeFile(String filePath) {
        try (FileInputStream fis = new FileInputStream(filePath)) {
            ParseResult<CompilationUnit> parseResult = javaParser.parse(fis);

            if (parseResult.isSuccessful() && parseResult.getResult().isPresent()) {
                CompilationUnit cu = parseResult.getResult().get();
                ClassAndMethodVisitor classAndMethodVisitor = new ClassAndMethodVisitor(methodCallGraph);
                cu.accept(classAndMethodVisitor, null);
            }
        } catch (Exception e) {
        }
    }

    public Map<String, Set<String>> getMethodCallGraph() {
        return methodCallGraph;
    }
}


