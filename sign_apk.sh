./gradlew assembleRelease
rm release.apk
zipalign -v -p 4 app/build/outputs/apk/app-release-unsigned.apk release.apk
apksigner sign --ks ~/Downloads/junrong.keystore release.apk
adb uninstall worker8.com.github.imgurblue
adb install release.apk

#bundle exec supply --json_key imgur_blue.json --package_name worker8.com.github.imgurblue --apk release.apk --track production --skip_upload_images --skip_upload_screenshots
