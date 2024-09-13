package voloshyn.android.domain

open class AppException:RuntimeException()

class DataStoreException():AppException()

class LocalStorageException():AppException()