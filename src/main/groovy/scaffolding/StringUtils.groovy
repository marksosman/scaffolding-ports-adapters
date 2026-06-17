package scaffolding

class StringUtils {

    static void validateRootPackage(String rootPackage) {
        if (!rootPackage?.matches(/[a-z][a-z0-9]*(\.[a-z][a-z0-9]*)*/)) {
            throw new IllegalArgumentException("rootPackage '${rootPackage}' is not a valid Java package name (e.g. com.example)")
        }
    }

    static String uncapitalize(String s) {
        if (!s) return s
        return s[0].toLowerCase() + s[1..-1]
    }

    static String convertCamelToSnakeCase(String s) {
        s.replaceAll(/([A-Z]+)([A-Z][a-z])/, '$1_$2')
         .replaceAll(/([a-z])([A-Z])/, '$1_$2')
         .toLowerCase()
    }
}
