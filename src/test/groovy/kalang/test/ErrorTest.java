package kalang.test;

import static org.junit.Assert.*;

import org.junit.Test;

import kalang.compiler.*;
//import kalang.Compiler as KC
import jast.ast.*;
import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;
import kalang.compiler.AstSemanticError;// as E;
//import kalang.tool.Compiler as TC;
public class ErrorTest {
	
    String outDir = "TestScript/generatedCode";
    String srcDir = "antlr/TestScript";
    String errSrcDir = "TestScript/error_src";
	
    int eCode;
	
    String errMsg;
	
    KalangCompiler kc;

    AstSemanticErrorHandler astSemanticErrorHandler = new AstSemanticErrorHandler() {
        @Override
        public void handleAstSemanticError(AstSemanticError error) {
            System.out.println("on error:" + error.getMsg());
                  eCode = error.getErrorCode();
            errMsg =error.getMsg();
        }
    };
	
    private void compile(String dir,String...name) throws IOException{
        eCode = -1;
        kc = new KalangCompiler(new JavaAstLoader());
        kc.setAstSemanticErrorHandler(astSemanticErrorHandler);
        for(String n : name){
            File src = new File(dir,n+".kl");//.readLines().join("\r\n");
            String source = FileUtils.readFileToString(src);
            kc.addSource(n,source);
        }
        kc.compile();
        System.out.println(kc.getJavaCodes());
    }
	
    private void cp(String... name) throws IOException{
        this.errMsg = null;
        compile(srcDir,name);
    }
	
    private void ecp(String... name) throws IOException{
        compile(errSrcDir,name);
    }
	
    @Test
    public void errorTest() throws IOException {
        //throw new RuntimeException("tt")
        //cp "NotImplemented"
        ecp("SyntaxError");
		
        ecp("ErrorAssign");
        assertEquals( eCode , AstSemanticError.UNABLE_TO_CAST);
        //TODO catch sematic error
        //ecp("NotImplemented");
        //assertEquals( eCode , AstSemanticError.CLASS_NOT_FOUND);
        ecp("NotImplemented","MyFace");
        assertEquals( eCode , AstSemanticError.METHOD_NOT_IMPLEMENTED);
    }
	
    @Test
    public void test() throws IOException{
        //cp("TestInput")
        cp("Base");
        //cp("HelloWorld","MyInterface")
        //cp "kava"
    }
	
    @Test
    public void toolTest(){
        //TC.main(this.errSrcDir,outDir)
    }
	

}
