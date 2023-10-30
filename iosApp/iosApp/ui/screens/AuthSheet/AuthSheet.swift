//
//  AuthSheet.swift
//  iosApp
//
//  Created by Максим Лейхнер on 28.10.2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI
import MultiPlatformLibrary
import MultiPlatformLibrarySwift
import mokoMvvmFlowSwiftUI


struct AuthSheet: ViewModifier {
    @Binding var isPresented: Bool
    func body(content: Content) -> some View {
        return ZStack(alignment: .bottom) {
            content
            if (isPresented) {
                AuthSheetContent()
            }
        }
    }
    
}

struct AuthSheetContent: View {
    @ObservedObject var viewModel = AuthViewModel()
    @State private var page: AuthPage = .emailOrPhone
    @Namespace var namespace
    var body: some View {
        page.view(emailORPhone: viewModel.binding(\.emailOrPhone), page: $page, namespace: namespace)
    }
}

enum AuthPage {
    case emailOrPhone
    case password
    case passkey
    
    @ViewBuilder func view(@Binding emailORPhone: String, @Binding page: AuthPage, namespace: Namespace.ID) -> some View {
        switch self {
        case .emailOrPhone:
            EmailOrPhoneStep(email: $emailORPhone, onNextPressed: { page = .password }, namespace: namespace)
        default:
            Text("7999")
                .matchedGeometryEffect(id: "emailOrPhone", in: namespace)
        }
    }
}

#Preview {
    @State var pres = true
    return Button("Open AuthSheet", action: {})
        .frame(maxWidth: .infinity, maxHeight: .infinity)
        .modifier(AuthSheet(isPresented: $pres))
}
