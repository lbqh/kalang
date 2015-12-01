package kalang;
import compilier.*
import jast.ast.*
import kalang.antlr.KalangLexer
import kalang.antlr.KalangParser
import kalang.core.VarObject
import kalang.core.VarTable
import kava.antlr.*

import org.antlr.v4.runtime.ANTLRInputStream
import org.antlr.v4.runtime.CommonTokenStream
import org.antlr.v4.runtime.tree.ParseTree

class Compiler {
    static void main(args) {
//        def src = args[0]
//        def out = args[1]
//        compile(new File(src),new File(out))
    }
    
    static List<File> getFiles(File dir){
        def list = []
        def files = dir.listFiles();
        for(File f in files){
            if(f.isDirectory()){
                list.addAll(getFiles(f))
            }else{
                list.add(f);
            }
        }
        return list;
    }
    
    static void compile(File srcDir,File outDir) {
        def astLoader = new JavaAstLoader();
        def cpl = new KalangCompiler(astLoader);
        def srcs = getFiles(srcDir);
        def abSrcPath = srcDir.getAbsolutePath()
        for(File s in srcs){
            def fname = s.getAbsolutePath();
            if(!fname.endsWith(".kl")) continue;
            def clsName = fname.substring(abSrcPath.length()+1,fname.length()-3).replace(File.separator,".");
            def txt = s.readLines().join("\r\n");
            cpl.addSource(clsName,txt)
        }
        try{
            cpl.compile();	
        }catch(CompileError e){
            def src = e.getSource();
            def ltext = src.substring(e.offset,e.offset + e.length +1)
            System.err.println e.message + " on ${ltext}"
        }
    }
}