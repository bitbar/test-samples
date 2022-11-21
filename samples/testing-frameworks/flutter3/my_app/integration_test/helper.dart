import 'dart:io';
import 'package:flutter/foundation.dart' show kIsWeb;

takeScreenshot(tester, binding, name) async {
  if (kIsWeb) {
    await binding.takeScreenshot(name);
    return;
  } else if (Platform.isAndroid) {
    await binding.convertFlutterSurfaceToImage();
    await tester.pumpAndSettle();
  }
  await binding.takeScreenshot(name);
}
