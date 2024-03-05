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
import java.util.*;

public class DependencyAnalyzer {
    private final Map<String, Set<String>> classDependencies = new HashMap<>();
    private final Map<String, Set<String>> methodCallGraph = new HashMap<>();
    private final JavaParser javaParser;
    private final String targetMethodQualifiedName;

    public DependencyAnalyzer(String targetMethodQualifiedName) {
        this.targetMethodQualifiedName = targetMethodQualifiedName;
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
                cu.accept(new ClassAndMethodVisitor(classDependencies, methodCallGraph, targetMethodQualifiedName), null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Map<String, Set<String>> getClassDependencies() {
        return classDependencies;
    }

    public Set<String> getCallHierarchy() {
        Set<String> callHierarchy = new HashSet<>();
        extractCallHierarchy(targetMethodQualifiedName, callHierarchy);
        return callHierarchy;
    }

    private void extractCallHierarchy(String methodName, Set<String> callHierarchy) {
        if (methodCallGraph.containsKey(methodName)) {
            Set<String> calledMethods = methodCallGraph.get(methodName);
            for (String calledMethod : calledMethods) {
                if (callHierarchy.add(calledMethod)) {
                    extractCallHierarchy(calledMethod, callHierarchy);
                }
            }
        }
    }
}



