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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import java.io.File;
import java.util.Map;
import java.util.Set;

public class YamlExporter {
    public static void exportToYaml(Map<String, Set<String>> data, String outputPath) {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        try {
            mapper.writeValue(new File(outputPath), data);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error exporting to YAML: " + e.getMessage());
        }
    }

    public static void exportMethodCallGraphToYaml(Map<String, Set<String>> data, String outputPath) {
        exportToYaml(data, outputPath);
    }
}

