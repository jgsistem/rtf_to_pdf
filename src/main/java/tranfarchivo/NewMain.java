package tranfarchivo;

import com.documents4j.api.DocumentType;
import com.documents4j.api.IConverter;
import com.documents4j.job.LocalConverter;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class NewMain {

public static void main(String[] args) throws FileNotFoundException,IOException, InterruptedException, ExecutionException
{
	System.out.println("Me acabas de pasar " + args.length + " parametros");
	System.out.println("Parametro 1 " +	args[0] );
	System.out.println("Parametro 2 " +	args[1] );
	
    String inputNombreArchivo = args[0];
    String outputNombreArchivo = args[1];
     
	
    ByteArrayOutputStream bo = new ByteArrayOutputStream();
    InputStream in = new BufferedInputStream(new FileInputStream(System.getProperty("user.dir") + File.separator + inputNombreArchivo));
    IConverter converter = LocalConverter.builder()
            .baseFolder(new File(System.getProperty("user.dir") + File.separator +"test"))
            .workerPool(20, 25, 2, TimeUnit.SECONDS)
            .processTimeout(5, TimeUnit.SECONDS)
            .build();

    Future<Boolean> conversion = converter
            .convert(in).as(DocumentType.RTF)
            .to(bo).as(DocumentType.PDF)
            .prioritizeWith(1000) // optional
            .schedule();
    conversion.get();

    try{          
    	 OutputStream outputStream = new FileOutputStream(outputNombreArchivo+".pdf");
    	    bo.writeTo(outputStream);      		
    	    in.close();
    	    bo.close();        
    }
    catch ( ArrayIndexOutOfBoundsException e )
    {            
        //si no existen parametros muestra error y termina programa
       // JOptionPane.showMessageDialog(null,"Error: No se pasaron argumentos. Necesita 2");
        //System.exit(0);
    }
   
}
}