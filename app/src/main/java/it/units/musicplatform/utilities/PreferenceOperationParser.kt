package it.units.musicplatform.utilities

import it.units.musicplatform.enumerations.Preference
import it.units.musicplatform.enumerations.PreferenceOperation
import it.units.musicplatform.enumerations.PreferenceOperation.*

class PreferenceOperationParser {

    companion object {


        fun changePreference(preference: Preference, postId: String, likeMap: HashMap<String, Boolean>, dislikeMap: HashMap<String, Boolean>): PreferenceOperation {
            return if (preference == Preference.LIKE) changeLike(postId, likeMap, dislikeMap) else changeDislike(postId, likeMap, dislikeMap)
        }


//        fun changePreference(preference: Preference, postId: String, likeMap: HashMap<String, Boolean>, dislikeMap: HashMap<String, Boolean>) = when {
//            !likeMap.containsKey(postId) and !dislikeMap.containsKey(postId) and (preference == Preference.LIKE) -> ADD_LIKE
//            likeMap.containsKey(postId) and !dislikeMap.containsKey(postId) and (preference == Preference.LIKE) -> REMOVE_LIKE
//            !likeMap.containsKey(postId) and dislikeMap.containsKey(postId) and (preference == Preference.LIKE) -> FROM_DISLIKE_TO_LIKE
//
//            !likeMap.containsKey(postId) and !dislikeMap.containsKey(postId) and (preference == Preference.DISLIKE) -> ADD_DISLIKE
//            !likeMap.containsKey(postId) and dislikeMap.containsKey(postId) and (preference == Preference.DISLIKE) -> REMOVE_DISLIKE
//            likeMap.containsKey(postId) and !dislikeMap.containsKey(postId) and (preference == Preference.DISLIKE) -> FROM_LIKE_TO_DISLIKE
//
//            else -> null //never happens
//
//        }

        private fun changeLike(postId: String, likeMap: HashMap<String, Boolean>, dislikeMap: HashMap<String, Boolean>) = when {
            !likeMap.containsKey(postId) and !dislikeMap.containsKey(postId) -> ADD_LIKE
            likeMap.containsKey(postId) and !dislikeMap.containsKey(postId) -> REMOVE_LIKE
            else -> FROM_DISLIKE_TO_LIKE
        }

        private fun changeDislike(postId: String, likeMap: HashMap<String, Boolean>, dislikeMap: HashMap<String, Boolean>) = when {
            !likeMap.containsKey(postId) and !dislikeMap.containsKey(postId) -> ADD_DISLIKE
            !likeMap.containsKey(postId) and dislikeMap.containsKey(postId) -> REMOVE_DISLIKE
            else -> FROM_LIKE_TO_DISLIKE
        }


    }

}