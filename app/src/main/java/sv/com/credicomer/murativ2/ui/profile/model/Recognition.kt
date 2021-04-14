package sv.com.credicomer.murativ2.ui.profile.model

data class Recognition(var sender:String? = null,
                       var receiver:List<String>? = null,
                       var message:String? = null,
                       var date:String? = null,
                       var like:List<String>? = null,
                       var id:String? = null,
                        var status: String = "")