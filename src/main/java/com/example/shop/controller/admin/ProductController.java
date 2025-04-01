package com.example.shop.controller.admin;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.shop.domain.Product;
import com.example.shop.service.ProductService;
import com.example.shop.service.UploadService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class ProductController {

    private final ProductService productService;
    private final UploadService uploadService;

    public ProductController(ProductService productService, UploadService uploadService) {
        this.productService = productService;
        this.uploadService = uploadService;
    }

    @GetMapping("/admin/product")
    public String getProductPage(Model model) {
        List<Product> products = productService.getAllProducts();
        model.addAttribute("products", products);
        return "admin/product/show";
    }

    @GetMapping("/admin/product/create")
    public String getCreateProductPage(Model model) {
        model.addAttribute("newProduct", new Product());
        return "admin/product/create";
    }

    @PostMapping("admin/product/create")
    public String createProduct(Model model, @ModelAttribute("newProduct") @Valid Product newProduct,
            BindingResult newProductBindingResult, @RequestParam("nameAvatarFile") MultipartFile file) {

        // validate

        if (newProductBindingResult.hasErrors()) {
            return "admin/product/create";
        }
        // upload image
        String image = this.uploadService.handleSaveUploadFile(file, "admin/images/product");
        newProduct.setImage(image);

        productService.handleSaveProduct(newProduct);
        return "redirect:admin/product";
    }

    @GetMapping("admin/product/{id}")
    public String getProductDetailPage(Model model, @PathVariable long id) {
        model.addAttribute("product", productService.fetchProductById(id).get());
        return "admin/product/detail";
    }

    @GetMapping("admin/product/delete/{id}")
    public String getProductDelete(Model model, @PathVariable long id) {
        model.addAttribute("deleteProduct", productService.fetchProductById(id).get());
        return "admin/product/delete";
    }

    @PostMapping("admin/product/delete")
    public String postDeleteProduct(Model model, @ModelAttribute("deleteProduct") Product deleteProduct) {
        // TODO: process POST request
        productService.handleDeleteProduct(deleteProduct.getId());
        return "redirect:admin/product";
    }

    @GetMapping("/admin/product/update/{id}")
    public String getProductUpdatePage(Model model, @PathVariable long id) {
        model.addAttribute("updateProduct", productService.fetchProductById(id).get());
        return "admin/product/update";
    }

    @PostMapping("admin/product/update")
    public String postUpdateProduct(Model model, @ModelAttribute("updateProduct") @Valid Product updateProduct,
            BindingResult updateProductBindingResult, @RequestParam("nameAvatarFile") MultipartFile file) {

        // validate
        if (updateProductBindingResult.hasErrors()) {
            return "admin/product/update";
        }
        // upload image
        if (!file.isEmpty()) {
            String image = this.uploadService.handleSaveUploadFile(file, "admin/images/product");
            updateProduct.setImage(image);
        }
        // TODO: process POST request
        productService.handleSaveProduct(updateProduct);
        return "redirect:admin/product";
    }

}
