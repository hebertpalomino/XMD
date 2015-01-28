//
//  SignUpViewController.h
//  accountably
//
//  Created by Steven Palomino on 1/12/15.
//  Copyright (c) 2015 Steven Palomino. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface SignUpViewController : UIViewController<UITextFieldDelegate>
@property (weak, nonatomic) IBOutlet UITextField *usernameLabel;
@property (weak, nonatomic) IBOutlet UITextField *passwordLabel;
- (IBAction)signupButton:(UIButton *)sender;

@end
