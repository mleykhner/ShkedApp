//
//  EmailOrPhoneStep.swift
//  iosApp
//
//  Created by Максим Лейхнер on 28.10.2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI

struct EmailOrPhoneStep: View {
    @Binding var email: String
    @State var isError: Bool = false
    @State var isButtonEnabled: Bool = true
    @State var isLoading: Bool = true
    @State var onNextPressed: () -> Void = {}
    let namespace: Namespace.ID
    var body: some View {
        VStack(alignment: .leading, spacing: 18) {
            VStack(alignment: .leading, spacing: 8) {
                Text("Давай знакомиться!")
                    .font(unboundedFontFamily.bold(size: 24))
                Text("Чтобы добавлять и просматривать задания нужно авторизоваться")
                    .font(golosFontFamily.regular(size: 16))
                    .frame(maxWidth: 300, alignment: .leading)
            }
            VStack(alignment: .center, spacing: 8) {
                VStack {
                    CustomTextField(title: "Почта", text: $email, isError: $isError)
                        .textContentType(.emailAddress)
                        .textInputAutocapitalization(.never)
                        .matchedGeometryEffect(id: "emailOrPhone", in: namespace)
                    Button("Продолжить", action: onNextPressed)
                        .buttonStyle(BigButtonStyle())
                        .disabled(!isButtonEnabled)
                }
                Text("Нажимая Продолжить, Вы принимаете политику конфиденциальности и пользовательское соглашение")
                    .font(golosFontFamily.regular(size: 11))
                    .opacity(0.5)
                    .multilineTextAlignment(.center)
            }
        }
        .padding(18)
    }
}

struct CustomTextField: View {
    @State var title: String
    @Binding var text: String
    @Binding var isError: Bool
    var body: some View {
        TextField(title, text: $text)
            .font(golosFontFamily.regular(size: 16))
            .padding(.leading, 16)
            .frame(height: 50)
            .background { Color("Error").opacity(isError ? 0.1 : 0.0) }
            .clipShape(RoundedRectangle(cornerRadius: 12, style: .continuous))
            .overlay {
                RoundedRectangle(cornerRadius: 12, style: .continuous)
                    .strokeBorder(Color(isError ? "Error" : "Outline"), lineWidth: isError ? 2 : 1)
            }
            .animation(.easeInOut, value: isError)
            .onChange(of: text, perform: { _ in isError = false })
    }
}

struct BigButtonStyle: ButtonStyle {
    @Environment(\.isEnabled) var isEnabled: Bool
    func makeBody(configuration: Configuration) -> some View {
        configuration.label
            .font(unboundedFontFamily.medium(size: 16))
            .foregroundStyle(Color.white)
            .frame(height: 50, alignment: .center)
            .frame(maxWidth: .infinity, alignment: .center)
            .background(Color("AccentColor").saturation(isEnabled ? 1.0 : 0.0))
            .clipShape(RoundedRectangle(cornerRadius: 25.0, style: .continuous))
            .animation(.easeInOut, value: isEnabled)
    }
}

//#Preview {
//    EmailOrPhoneStep()
//}
