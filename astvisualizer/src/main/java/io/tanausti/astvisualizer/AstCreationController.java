package io.tanausti.astvisualizer;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.io.IOException;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AstCreationController {

	@PostMapping("/parse/clang")
	public String serveClangAst(@RequestBody String code) {
		
		ClangAstCreator clangAstCreator = new ClangAstCreator();
		String ast = clangAstCreator.generateAst(code);
		return ast;
		
	}
	
	@PostMapping("/parse/kiln")
	public String serveKilnAst(@RequestBody String code) {
		
		
		KilnAstCreator kilnAstCreator = new KilnAstCreator();
		String ast = kilnAstCreator.generateAst(code);
		
		
		return ast;
		
	}
	
	
}
