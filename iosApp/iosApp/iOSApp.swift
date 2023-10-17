import SwiftUI
import MultiPlatformLibrary

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
