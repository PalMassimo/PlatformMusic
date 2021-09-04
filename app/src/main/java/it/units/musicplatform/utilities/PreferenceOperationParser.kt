package it.units.musicplatform.utilities

import it.units.musicplatform.enumerations.Preference
import it.units.musicplatform.enumerations.PreferenceOperation
import it.units.musicplatform.enumerations.PreferenceOperation.*

class PreferenceOperationParser {

    companion object {

        fun changePreference(preference: Preference, postId: String, likeMap: HashMap<String, Boolean>, dislikeMap: HashMap<String, Boolean>): PreferenceOperation {
            return if (preference == Preference.LIKE) changeLike(postId, likeMap, dislikeMap) else changeDislike(postId, likeMap, dislikeMap)
        }

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