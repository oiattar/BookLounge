package com.issa.omar.booklounge.rest

import com.google.gson.*
import com.issa.omar.booklounge.model.BookDetails
import java.lang.reflect.Type

class BookDetailsDeserializer : JsonDeserializer<BookDetails> {

    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): BookDetails {
        val jsonObject: JsonObject? = json?.asJsonObject
        val keys = jsonObject?.keySet()
        val bookDetails: JsonElement = jsonObject!!.get(keys?.first()).asJsonObject.get("details")
        return Gson().fromJson(bookDetails, typeOfT)
    }
}