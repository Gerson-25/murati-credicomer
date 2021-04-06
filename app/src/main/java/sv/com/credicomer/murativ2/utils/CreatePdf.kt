package sv.com.credicomer.murativ2.utils

import android.content.Context
import android.os.Environment
import android.widget.Toast
import com.uttampanchasara.pdfgenerator.CreatePdf

class PdfCreator{
    fun createPfd(context: Context, html:String){
        CreatePdf(context)
            .setPdfName("request")
            .setContent(html)
            .setFilePath(Environment.getRootDirectory().absolutePath+ "MyPath")
            .setCallbackListener(object : CreatePdf.PdfCallbackListener{
                override fun onFailure(errorMsg: String) {
                    Toast.makeText(context, "Pdf fue creado", Toast.LENGTH_SHORT).show()
                }

                override fun onSuccess(filePath: String) {
                    Toast.makeText(context, "Pdf no fue creado", Toast.LENGTH_SHORT).show()
                }

            })
    }
}