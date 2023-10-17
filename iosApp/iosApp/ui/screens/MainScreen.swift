//
//  MainScreen.swift
//  iosApp
//
//  Created by Максим Лейхнер on 12.08.2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI
import MultiPlatformLibrary

struct MainScreen: View {

    @State var selectedScreen: String = tabs[1].path
    
    var body: some View {
        ZStack(alignment: .bottom) {
            TabView(selection: $selectedScreen) {
                ForEach(tabs, id: \.self) { tab in
                    SignInScreen(onDismiss: {}, onSignUpRequested: {})
//                    Text(tab.name)
//                        .tag(tab.path)
                }
            }.tabViewStyle(.automatic)
            HStack(alignment: .bottom) {
                Spacer()
                ForEach(tabs, id: \.self) { tab in
                    VStack(spacing: 4) {
                        Image(systemName: tab.iconSystemName)
                            .font(.system(size: 24))
                        Text(tab.name)
                            .font(unboundedFontFamily.medium(size: 12))
                    }
                    .frame(width: 95)
                    .foregroundStyle(Color(selectedScreen == tab.path ? "primary" : "onPrimaryContainer"))
                    .animation(.easeInOut, value: selectedScreen)
                    .onTapGesture {
                        selectedScreen = tab.path
                    }
                    Spacer()
                }
            }
            .padding([.top], 8)
            .ignoresSafeArea()
            .background(Material.bar)
            
        }
    }
}

#Preview {
    return MainScreen()
}

struct Tab: Hashable {
    let path: String
    let iconSystemName: String
    let name: String
}

let tabs = [
    Tab(path: "news", iconSystemName: "newspaper", name: Strings().get(id: SharedRes.strings().news, args: [])),
    Tab(path: "schedule", iconSystemName: "list.bullet.below.rectangle", name: Strings().get(id: SharedRes.strings().schedule, args: [])),
    Tab(path: "tasks", iconSystemName: "magazine", name: Strings().get(id: SharedRes.strings().tasks, args: []))
]


