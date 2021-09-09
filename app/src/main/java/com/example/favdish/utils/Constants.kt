package com.example.favdish.utils

object Constants {

    val DISH_TYPE:String="DishType"
    val DISH_CATEGORY="DISH_CATEGORY"
    val DISH_COOKING_TIME:String="DishCookingTime"
    val DISH_IMAGE_SOURCE_LOCAL="local"
    val DISH_IMAGE_SOURCE_ONLINE="online"

    val EDIT_DISH="EDIT_DISH"

    const val API_ENDPOINT="/recipes/random"
    const val API_KEY="apiKey"
    const val LIMIT_LICENSE="limitLicense"
    const val TAGS="tags"
    const val NUMBER="number"

    const val BASE_URL="https://api.spoonacular.com"

    const val API_KEY_VALUE:String="6f89a76ee93042a280e615fc1fa6d72a"
    const val L_L_VALUE:Boolean=true
    const val TAGS_VALUE:String="vegetarian,dessert"
    const val NUMBER_VALUE:Int=1

    const val Notification_id="FavDish_Notification_id"
    const val Notification_name="FavDish_Notification_name"
    const val Notification_channel="FavDish_channel_01"


    fun dishTypes():ArrayList<String>{
       val list=ArrayList<String>()
        list.add("breakfash")
        list.add("lunch")
        list.add("snaks")
        list.add("dinner")
        list.add("salad")
        list.add("side dish")
        list.add("dessert")
        list.add("other")
       return list
    }

    fun dishCategory():ArrayList<String>{
        val list=ArrayList<String>()
        list.add("Pizza")
        list.add("bbq")
        list.add("bakery")
        list.add("burger")
        list.add("cafee")
        list.add("chicken")
        list.add("dessert")
        list.add("drink")
        list.add("hot dog")
        list.add("juice")
        list.add("sandwich")
        list.add("Tea cofee")
        list.add("wraps")
        list.add("other")

        return list
    }

    fun dishCookingTime():ArrayList<String>{
        val list=ArrayList<String>()
        list.add("10")
        list.add("15")
        list.add("20")
        list.add("30")
        list.add("35")
        list.add("40")
        list.add("50")
        list.add("60")
        list.add("90")
        list.add("120")
        list.add("150")
        list.add("180")

        return list
    }
}