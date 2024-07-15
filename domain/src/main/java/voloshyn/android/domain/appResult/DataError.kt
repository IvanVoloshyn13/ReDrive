package voloshyn.android.domain.appResult

interface DataError : AppError {
    enum class Locale : DataError {
        DATA_NOT_FOUND, STORAGE_ERROR,UNKNOWN_ERROR
    }
}