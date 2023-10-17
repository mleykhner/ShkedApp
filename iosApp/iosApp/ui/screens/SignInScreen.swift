//
//  SignInScreen.swift
//  iosApp
//
//  Created by Максим Лейхнер on 17.10.2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI
import mokoMvvmFlowSwiftUI
import MultiPlatformLibrary

struct SignInScreen: View {
    @ObservedObject var viewModel: SignInViewModel = SignInViewModel()
    private let sr = SharedRes.strings()
    var body: some View {
        VStack(alignment: .leading) {
            Text(Strings().get(id: sr.auth_screen_heading, args: []))
                .font(unboundedFontFamily.bold(size: 20))
            Spacer().frame(height: 18)
            Text(Strings().get(id: sr.auth_screen_description, args: []))
                .font(golosFontFamily.regular(size: 16))
            TextField(Strings().get(id: sr.email, args: []), text: viewModel.binding(\.email))
                .textContentType(.emailAddress)
                .keyboardType(.emailAddress)
                .padding(16)
                .background(Color.gray.opacity(0.5))
                .clipShape(RoundedRectangle(cornerRadius: 8, style: .continuous))
            SecureField(Strings().get(id: sr.password, args: []), text: viewModel.binding(\.password))
                .textContentType(.password)
                .padding(16)
                .background(Color.gray.opacity(0.5))
                .clipShape(RoundedRectangle(cornerRadius: 8, style: .continuous))
            Button(Strings().get(id: sr.sign_in_verb, args: []), action: viewModel.onLoginPressed)
                .buttonStyle(.borderedProminent)
                .buttonBorderShape(.capsule)
                .disabled(!viewModel.state(\.isButtonEnabled))

        }
        .padding(20)
    }
}

#Preview {
    SignInScreen()
}
