import SwiftUI
import shared


let labelMediumStyle = Font(SharedRes.fontsUnbounded.shared.medium.uiFont(withSize: 12))
let titleMediumStyle = Font(SharedRes.fontsUnbounded.shared.bold.uiFont(withSize: 20))
let titleSmallStyle = Font(SharedRes.fontsUnbounded.shared.bold.uiFont(withSize: 10))
let labelSmallStyle = Font(SharedRes.fontsGolos.shared.medium.uiFont(withSize: 12))
let bodyMediumStyle = Font(SharedRes.fontsGolos.shared.regular.uiFont(withSize: 14))
let bodyLargeStyle = Font(SharedRes.fontsGolos.shared.regular.uiFont(withSize: 16))

struct unboundedFontFamily {
    static func regular(size: Double) -> Font {
        return Font(SharedRes.fontsUnbounded.shared.regular.uiFont(withSize: size))
    }
    
    static func medium(size: Double) -> Font {
        return Font(SharedRes.fontsUnbounded.shared.medium.uiFont(withSize: size))
    }
    
    static func semiBold(size: Double) -> Font {
        return Font(SharedRes.fontsUnbounded.shared.semiBold.uiFont(withSize: size))
    }
    
    static func bold(size: Double) -> Font {
        return Font(SharedRes.fontsUnbounded.shared.bold.uiFont(withSize: size))
    }
}

struct golosFontFamily {
    static func regular(size: Double) -> Font {
        return Font(SharedRes.fontsGolos.shared.regular.uiFont(withSize: size))
    }
    
    static func medium(size: Double) -> Font {
        return Font(SharedRes.fontsGolos.shared.medium.uiFont(withSize: size))
    }
    
    static func semiBold(size: Double) -> Font {
        return Font(SharedRes.fontsGolos.shared.semiBold.uiFont(withSize: size))
    }
    
    static func bold(size: Double) -> Font {
        return Font(SharedRes.fontsGolos.shared.bold.uiFont(withSize: size))
    }
}
