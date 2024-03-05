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
        // Define the root directory containing the Java source files
        String rootDirectory = "path/to/your/java/source";
        
        // Specify the fully qualified name of the target method for call hierarchy analysis
        String targetMethodQualifiedName = "com.example.MyClass.myMethod";

        // Collect all Java source files from the specified directory
        List<String> javaFiles = JavaFileCollector.collectJavaFiles(rootDirectory);

        // Initialize the DependencyAnalyzer with the target method's fully qualified name
        DependencyAnalyzer analyzer = new DependencyAnalyzer(targetMethodQualifiedName);
        
        // Analyze each collected file
        for (String filePath : javaFiles) {
            analyzer.analyzeFile(filePath);
        }

        // Extract the class dependencies and the call hierarchy for the specified method
        Map<String, Set<String>> classDependencies = analyzer.getClassDependencies();
        Set<String> callHierarchy = analyzer.getCallHierarchy();

        // Export the collected class dependencies to a YAML file
        YamlExporter.exportToYaml(classDependencies, "output/class_dependencies.yaml");

        // Export the extracted call hierarchy to a YAML file
        YamlExporter.exportMethodCallGraphToYaml(Map.of(targetMethodQualifiedName, callHierarchy), "output/method_call_hierarchy.yaml");

        // Optionally, print the extracted information to the console
        System.out.println("Class Dependencies:");
        classDependencies.forEach((key, value) -> System.out.println(key + " -> " + value));

        System.out.println("\nCall Hierarchy for " + targetMethodQualifiedName + ":");
        callHierarchy.forEach(System.out::println);
    }
}

