import SwiftUI
import shared

@main
struct iOSApp: App {
    
    init() {
        HelperKt.doInitKoin()
        StartNapierKt.startNapier()
    }
    
	var body: some Scene {
		WindowGroup {
			MainScreen()
		}
	}
}
