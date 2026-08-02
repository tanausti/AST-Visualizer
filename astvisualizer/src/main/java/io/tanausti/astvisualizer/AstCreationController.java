package io.tanausti.astvisualizer;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.io.IOException;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AstCreationController {

	@PostMapping("/parse")
	public String generateAstfromCode(@RequestBody String code) {
		
		String astDumpJson = clangAstDump(code);
		
		if(astDumpJson == null) {
			return "ERROR";
		}
		else {
		
			AstParser astParser = new AstParser();
			String finalAst = astParser.parseAst(astDumpJson);
			return finalAst;
		}
		
		
		
	}

	
	// call when user submits code
	private String clangAstDump(String code) {
		
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
					sb.append(line).append("\n");
				}
				
			
			}

			// Wait for the process to complete and get the exit code
			int exitCode = process.waitFor();
			System.out.println("\nExited with error code: " + exitCode);
			
			
			if(exitCode == 0) {
				return sb.toString();
			}
			else {
				return null;
			}

		} catch (IOException e) {
			System.err.println("Failed to execute command: " + e.getMessage());
		} catch (InterruptedException e) {
			System.err.println("Process was interrupted: " + e.getMessage());
			Thread.currentThread().interrupt(); // Restore interrupted status
		}
		
		return null;
		

	}

}
