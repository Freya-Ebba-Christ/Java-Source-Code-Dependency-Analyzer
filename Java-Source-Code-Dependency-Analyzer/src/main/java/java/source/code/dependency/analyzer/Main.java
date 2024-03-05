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
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Main {
    public static void main(String[] args) {
        String rootDirectory = "path/to/your/java/source";
        List<String> javaFiles = JavaFileCollector.collectJavaFiles(rootDirectory);

        DependencyAnalyzer analyzer = new DependencyAnalyzer();
        for (String filePath : javaFiles) {
            analyzer.analyzeFile(filePath);
        }

        Map<String, Set<String>> methodCallGraph = analyzer.getMethodCallGraph();
        YamlExporter.exportMethodCallGraphToYaml(methodCallGraph, "output/method_call_graph.yaml");
    }
}