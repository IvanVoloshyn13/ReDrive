package voloshyn.android.data


fun String.valueId(): String {
    return this.split("|")[0]
}