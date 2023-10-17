import SwiftUI
import MultiPlatformLibrary

@main
struct iOSApp: App {
    
    init() {
        print("App started")
        HelperKt.doInitKoin()
        StartNapierKt.startNapier()
    }
    
	var body: some Scene {
		WindowGroup {
			MainScreen()
		}
	}
}
