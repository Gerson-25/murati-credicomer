package sv.com.credicomer.murativ2.ui.profile.model

data class Recognition(var sender:String? = null,
                       var receiver:String? = null,
                       var message:String? = null,
                       var date:String? = null,
                       var like:Boolean? = null,
                       var id:String? = null)