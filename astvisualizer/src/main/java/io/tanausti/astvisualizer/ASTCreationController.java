package io.tanausti.astvisualizer;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.io.IOException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ASTCreationController {    
    
    //call when user submits code
    @PostMapping("/parse")
    public void clangASTDump(@RequestBody String code) {

        Path codeFile = null;
        try {
            codeFile = Files.createTempFile("code", ".c");
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        try {
            Files.writeString(codeFile, code);
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        ProcessBuilder pb = new ProcessBuilder(
            "clang",
            "-Xclang",
            "-ast-dump",
            "-fsyntax-only",
            codeFile.toString()
        );
        
        System.out.print(codeFile);

        // Merge standard error stream with standard output stream
        pb.redirectErrorStream(true);
        
        invokeClang(pb);
        
    }
    
    private void invokeClang(ProcessBuilder pb) {
        
        try {
            // Start the process
            Process process = pb.start();

            // Read the output of the command
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                }
            }

            // Wait for the process to complete and get the exit code
            int exitCode = process.waitFor();
            System.out.println("\nExited with error code: " + exitCode);

        } catch (IOException e) {
            System.err.println("Failed to execute command: " + e.getMessage());
        } catch (InterruptedException e) {
            System.err.println("Process was interrupted: " + e.getMessage());
            Thread.currentThread().interrupt(); // Restore interrupted status
        }


    }    
        
       
    }
       
        
        


    