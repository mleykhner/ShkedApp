package ru.mleykhner.shkedapp.data.remote

object HttpRoutes {
    private const val BASE_URL = "https://shkedapp.ru"
    private const val BASE_URL_WS = "wss://shkedapp.ru:7195"
    private const val API = "$BASE_URL/API"
    private const val AUTH = "$API/Auth"
    const val AUTH_SIGN_UP = "$AUTH/SignUp"
    const val AUTH_SIGN_IN = "$AUTH/SignIn"
    const val AUTH_REFRESH = "$AUTH/Refresh"
    const val AUTH_LOGOUT = "$AUTH/Logout"
    const val AUTH_LOGOUT_FROM_ALL = "$AUTH/LogoutFromAll"
    const val GROUPS = "$BASE_URL/Groups"
    const val SCHEDULE = "$BASE_URL_WS/GroupsSchedule"
}
