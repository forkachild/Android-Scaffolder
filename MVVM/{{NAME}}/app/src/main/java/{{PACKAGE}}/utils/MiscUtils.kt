package {{PACKAGE}}.utils

fun <T1, T2> safeLet(t1: T1?, t2: T2?, block: (t1: T1, t2: T2) -> Unit) {
    if (t1 != null && t2 != null) {
        block(t1, t2)
    }
}