package voloshyn.android.domain

open class AppException:RuntimeException()

class DataStoreException():AppException()

class LocalStorageException():AppException()

class FileNotFoundException():AppException()

class UnknownException():AppException()

class IsCurrentVehicleException():AppException()