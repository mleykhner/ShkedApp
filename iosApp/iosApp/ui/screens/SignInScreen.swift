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
import MultiPlatformLibrarySwift

struct SignInScreen: View {

    private let sr = SharedRes.strings()
    private enum Field: Hashable {
        case usernameField
        case passwordField
    }

    @ObservedObject private var viewModel: SignInViewModel = SignInViewModel()
    @FocusState private var focusedField: Field?
    @State private var onDismiss: () -> Void
    @State private var onSignUpRequested: () -> Void

    init(onDismiss: @escaping () -> Void, onSignUpRequested: @escaping () -> Void) {
        self.onDismiss = onDismiss
        self.onSignUpRequested = onSignUpRequested
        focusedField = .usernameField
    }

    var body: some View {
        VStack(alignment: .leading) {
            Text(Strings().get(id: sr.auth_screen_heading, args: []))
                .font(unboundedFontFamily.bold(size: 24))
            Spacer().frame(height: 10)
            Text(Strings().get(id: sr.auth_screen_description, args: []))
                .font(golosFontFamily.regular(size: 16))
                .frame(width: 300, alignment: .leading)
            Spacer().frame(height: 32)
            VStack (spacing: 8) {
                TextField(Strings().get(id: sr.email, args: []), text: viewModel.binding(\.email))
                    .font(golosFontFamily.regular(size: 16))
                    .textContentType(.emailAddress)
                    .keyboardType(.emailAddress)
                    .autocorrectionDisabled()
                    .submitLabel(.next)
                    .focused($focusedField, equals: .usernameField)
                    .onSubmit {
                        focusedField = .passwordField
                    }
                    .padding(16)
                    .overlay { RoundedRectangle(cornerRadius: 8, style: .continuous).strokeBorder(Color.gray, lineWidth: 2)}

                SecureField(Strings().get(id: sr.password, args: []), text: viewModel.binding(\.password))
                    .font(golosFontFamily.regular(size: 16))
                    .textContentType(.password)
                    .submitLabel(.done)
                    .focused($focusedField, equals: .passwordField)
                    .padding(16)
                    .overlay { RoundedRectangle(cornerRadius: 8, style: .continuous).strokeBorder(Color.gray, lineWidth: 2)}
            }
            Spacer().frame(height: 18)
            HStack {
                Button(Strings().get(id: sr.sign_in_verb, args: []), action: viewModel.onLoginPressed)
                    .font(golosFontFamily.regular(size: 16))
                    .controlSize(.large)
                    .buttonStyle(.borderedProminent)
                    .buttonBorderShape(.capsule)
                    .disabled(!viewModel.state(\.isButtonEnabled))
                Spacer()
                Button(Strings().get(id: sr.sign_up_noun, args: []), action: {})
                    .font(golosFontFamily.regular(size: 16))
                    .controlSize(.large)
                    .buttonStyle(.bordered)
                    .buttonBorderShape(.capsule)
            }
        }
        .padding(20)
        .onReceive(createPublisher(viewModel.actions)) { action in
            let actionKs = SignInViewModelActionKs(action)
            switch(actionKs) {
            case .loginSuccess:
                print("Success")
                onDismiss()
                break
            @unknown default:
                fatalError()
            }
        }
    }
}

#Preview {
    @State var showSheet = true
    return Button("Sign In", action: { showSheet.toggle() })
        .sheet(isPresented: $showSheet, content: {
            SignInScreen(onDismiss: { showSheet.toggle() }, onSignUpRequested: {})
        })
}
