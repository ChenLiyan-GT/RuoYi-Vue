# 通用API

**本文档引用的文件**
- [CommonController.java](../../../ruoyi-admin/src/main/java/com/ruoyi/web/controller/common/CommonController.java)
- [RuoYiConfig.java](../../../ruoyi-common/src/main/java/com/ruoyi/common/config/RuoYiConfig.java)
- [FileUploadUtils.java](../../../ruoyi-common/src/main/java/com/ruoyi/common/utils/file/FileUploadUtils.java)
- [FileUtils.java](../../../ruoyi-common/src/main/java/com/ruoyi/common/utils/file/FileUtils.java)
- [MimeTypeUtils.java](../../../ruoyi-common/src/main/java/com/ruoyi/common/utils/file/MimeTypeUtils.java)
- [ServerConfig.java](../../../ruoyi-common/src/main/java/com/ruoyi/common/config/ServerConfig.java)
- [Constants.java](../../../ruoyi-common/src/main/java/com/ruoyi/common/constant/Constants.java)
- [application.yml](../../../ruoyi-admin/src/main/resources/application.yml)

## 目录

1. [简介](#简介)
2. [API 端点](#api-端点)
3. [文件上传限制与校验](#文件上传限制与校验)
4. [文件存储路径](#文件存储路径)
5. [错误处理](#错误处理)

## 简介

通用 API 由 `CommonController` 提供，挂载在 `/common` 路径下，负责处理系统中的通用文件操作：文件上传（单文件/多文件）、文件下载和本地资源下载。该控制器不依赖 Shiro 权限校验，属于公共匿名端点。

## API 端点

### 单文件上传

上传单个文件到服务器。

- **HTTP 方法**: `POST`
- **路径**: `/common/upload`
- **Content-Type**: `multipart/form-data`
- **请求参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| file | MultipartFile | 是 | 上传的文件 |

- **响应示例** (`AjaxResult`):

```json
{
  "code": 0,
  "msg": "操作成功",
  "url": "http://localhost/profile/upload/2026/07/21/文件名_序列.扩展名",
  "fileName": "/profile/upload/2026/07/21/原文件名_序列.扩展名",
  "newFileName": "原文件名_序列.扩展名",
  "originalFilename": "原文件名.扩展名"
}
```

- **说明**: 上传路径由 `RuoYiConfig.getUploadPath()` 决定，返回的 `url` 通过 `ServerConfig.getUrl()` 拼接当前请求域名。

> 来源：[CommonController.java(L75-L97)](../../../ruoyi-admin/src/main/java/com/ruoyi/web/controller/common/CommonController.java)(L75-L97)

### 多文件上传

同时上传多个文件。

- **HTTP 方法**: `POST`
- **路径**: `/common/uploads`
- **Content-Type**: `multipart/form-data`
- **请求参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| files | List\<MultipartFile\> | 是 | 多个文件 |

- **响应示例** (`AjaxResult`):

```json
{
  "code": 0,
  "msg": "操作成功",
  "urls": "url1,url2,url3",
  "fileNames": "/profile/upload/2026/07/21/f1.jpg,/profile/upload/2026/07/21/f2.jpg",
  "newFileNames": "f1.jpg,f2.jpg",
  "originalFilenames": "原文件1.jpg,原文件2.jpg"
}
```

- **说明**: 多个文件的返回字段名带 `s` 后缀，各值以逗号 `,` 分隔。

> 来源：[CommonController.java(L102-L135)](../../../ruoyi-admin/src/main/java/com/ruoyi/web/controller/common/CommonController.java)(L102-L135)

### 文件下载

从服务器下载已存储的文件。

- **HTTP 方法**: `GET`
- **路径**: `/common/download`
- **请求参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| fileName | String | 是 | 文件名称（含路径） |
| delete | Boolean | 否 | 下载后是否删除源文件 |

- **说明**: 文件路径为 `RuoYiConfig.getDownloadPath() + fileName`。下载前通过 `FileUtils.checkAllowDownload()` 校验文件名合法性。响应 Content-Type 为 `application/octet-stream`，并设置附件下载头。

> 来源：[CommonController.java(L46-L70)](../../../ruoyi-admin/src/main/java/com/ruoyi/web/controller/common/CommonController.java)(L46-L70)

### 本地资源下载

下载已上传的本地资源文件（如用户头像、导入文件等）。

- **HTTP 方法**: `GET`
- **路径**: `/common/download/resource`
- **请求参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| resource | String | 是 | 资源路径（相对于 profile 目录） |

- **说明**: 资源路径为 `RuoYiConfig.getProfile() + resource`。同样会进行文件名合法性校验，响应为 `application/octet-stream` 流。

> 来源：[CommonController.java(L140-L163)](../../../ruoyi-admin/src/main/java/com/ruoyi/web/controller/common/CommonController.java)(L140-L163)

## 文件上传限制与校验

上传流程通过 `FileUploadUtils` 执行以下校验：

| 校验项 | 限制值 | 说明 |
|--------|--------|------|
| 单文件大小 | 50 MB | `DEFAULT_MAX_SIZE` 常量定义 |
| 文件名长度 | ≤ 100 字符 | `DEFAULT_FILE_NAME_LENGTH` |
| 单个请求大小 | 20 MB | `spring.servlet.multipart.max-request-size` |
| 单个文件大小（Spring） | 10 MB | `spring.servlet.multipart.max-file-size` |

**允许上传的文件扩展名**（来自 `MimeTypeUtils.DEFAULT_ALLOWED_EXTENSION`）：

| 分类 | 扩展名 |
|------|--------|
| 图片 | bmp, gif, jpg, jpeg, png |
| 文档 | doc, docx, xls, xlsx, ppt, pptx, txt |
| 压缩 | rar, zip, gz, bz2 |
| 视频 | mp4, avi, rmvb |
| 其他 | pdf |

> 来源：[FileUploadUtils.java(L26-L34)](../../../ruoyi-common/src/main/java/com/ruoyi/common/utils/file/FileUploadUtils.java)(L26-L34)、[MimeTypeUtils.java(L29-L39)](../../../ruoyi-common/src/main/java/com/ruoyi/common/utils/file/MimeTypeUtils.java)(L29-L39)、[application.yml(L64-L69)](../../../ruoyi-admin/src/main/resources/application.yml)(L64-L69)

## 文件存储路径

文件存储路径由 `RuoYiConfig` 统一管理，通过 `application.yml` 中的 `ruoyi.profile` 配置：

| 路径方法 | 实际路径 | 用途 |
|----------|----------|------|
| `getProfile()` | `D:/giencoder/RuoYi-springboot3` | 基础目录 |
| `getUploadPath()` | `{profile}/upload` | 文件上传 |
| `getDownloadPath()` | `{profile}/download/` | 文件下载 |
| `getImportPath()` | `{profile}/import` | 数据导入 |
| `getAvatarPath()` | `{profile}/avatar` | 用户头像 |

上传的文件按日期目录组织，文件名编码格式为：`日期目录/原文件名_序列值.扩展名`，其中序列值由 `Seq.getId(Seq.uploadSeqType)` 生成。

上传成功后返回的 URL 路径以 `/profile` 前缀开头（`Constants.RESOURCE_PREFIX`），该前缀需配合静态资源映射才能直接访问。

> 来源：[RuoYiConfig.java(L93-L123)](../../../ruoyi-common/src/main/java/com/ruoyi/common/config/RuoYiConfig.java)(L93-L123)、[Constants.java(L95)](../../../ruoyi-common/src/main/java/com/ruoyi/common/constant/Constants.java)(L95)、[FileUploadUtils.java(L144-L147)](../../../ruoyi-common/src/main/java/com/ruoyi/common/utils/file/FileUploadUtils.java)(L144-L147)

## 错误处理

上传过程中的异常通过 `GlobalExceptionHandler` 统一处理，常见异常类型：

| 异常类 | 触发条件 |
|--------|----------|
| `FileSizeLimitExceededException` | 文件大小超过 50 MB |
| `FileNameLengthLimitExceededException` | 文件名长度超过 100 字符 |
| `InvalidExtensionException` | 文件扩展名不在允许列表中 |
| `InvalidImageExtensionException` | 图片类扩展名不合法（`InvalidExtensionException` 子类） |
| `FileUploadException` | 通用文件上传异常 |

> 来源：[FileUploadUtils.java(L86-L104)](../../../ruoyi-common/src/main/java/com/ruoyi/common/utils/file/FileUploadUtils.java)(L86-L104)、[exception/file 包](../../../ruoyi-common/src/main/java/com/ruoyi/common/exception/file/)