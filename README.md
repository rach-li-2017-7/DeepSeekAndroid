# DeepSeek Android

This Android application runs the DeepSeek-R1-Distill-Qwen-1.5B model locally on Android devices. The app provides a simple interface for text generation using the model.

## Features

- Local inference using DeepSeek-R1-Distill-Qwen-1.5B model
- Simple and intuitive UI built with Jetpack Compose
- Efficient model loading and management
- Support for text generation with custom prompts

## Setup Instructions

1. Clone this repository
2. Download the model files:
   - Download the PyTorch model file (`deepseek_model.pt`) from HuggingFace
   - Download the tokenizer file (`tokenizer.json`) from HuggingFace
3. Place the model files in the `app/src/main/assets` directory
4. Build and run the app using Android Studio

## Building for Release

1. Update the `app/build.gradle` file with your signing configuration
2. Build the release APK:
   ```bash
   ./gradlew assembleRelease
   ```
3. Sign the APK using your keystore
4. Test the release build thoroughly before publishing

## Publishing to Google Play

1. Create a Google Play Developer account if you haven't already
2. Create a new application in the Google Play Console
3. Follow the Google Play Console instructions to:
   - Fill in the store listing
   - Upload screenshots and promotional materials
   - Configure content rating
   - Set up pricing and distribution
4. Upload the signed APK or Android App Bundle
5. Submit for review

## Technical Requirements

- Android 7.0 (API level 24) or higher
- At least 2GB of RAM
- Around 1GB of storage space for the model

## License

This project is licensed under the terms of the MIT license. See the LICENSE file for details.

## Acknowledgments

- DeepSeek AI for the original model
- HuggingFace for model hosting and tokenizer
- PyTorch team for PyTorch Mobile
