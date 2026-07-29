package io.tanausti.astvisualizer;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.io.IOException;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ASTCreationController {

	@PostMapping("/parse")
	public String ASTJSONResponse(@RequestBody String code) {
		
		String astDump = clangASTDump(code);
		
		ASTParser astParser = new ASTParser();
		String json = astParser.parseAST(astDump);
		
		return json;
		
		
	}

	
	// call when user submits code
	private String clangASTDump(String code) {

		Path codeFile = null;
		try {
			codeFile = Files.createTempFile("code", ".c");
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			Files.writeString(codeFile, code);
		} catch (IOException e) {
			e.printStackTrace();
		}

		ProcessBuilder pb = new ProcessBuilder("clang", "-Xclang", "-ast-dump=json", "-fsyntax-only", codeFile.toString());

		System.out.print(codeFile);

		// Merge standard error stream with standard output stream
		pb.redirectErrorStream(true);

		String astDump = invokeClang(pb);
		
		
		return astDump;

	}

	private String invokeClang(ProcessBuilder pb) {

		try {
			// Start the process
			Process process = pb.start();
			
			StringBuilder sb;

			// Read the output of the command
			try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {

				sb = new StringBuilder();
				String line;
				
				while ((line = reader.readLine()) != null) {
					sb.append(line + "\n");
				}
				
			
			}

			// Wait for the process to complete and get the exit code
			int exitCode = process.waitFor();
			System.out.println("\nExited with error code: " + exitCode);
			
			return sb.toString();

		} catch (IOException e) {
			System.err.println("Failed to execute command: " + e.getMessage());
		} catch (InterruptedException e) {
			System.err.println("Process was interrupted: " + e.getMessage());
			Thread.currentThread().interrupt(); // Restore interrupted status
		}
		
		return null;
		

	}

}
