#!/usr/bin/env python
# -*- coding: utf-8 -*-
import os
import sys

from os.path import abspath, dirname, join as path_join

from robot import run_cli

CURDIR = abspath(dirname(__file__))
LIBROOT = path_join(CURDIR, 'libs')
ESCAPED_CURDIR = CURDIR.replace(' ', '!')
DEFAULT_ARGS = '--escape space:! --variable PROJECTROOT:{root} {source}'.format(
    root=ESCAPED_CURDIR, source=path_join(ESCAPED_CURDIR, 'tests-android'))

def extend_python_path():
    sys.path.append(LIBROOT)

def main(cli_args):
    extend_python_path()
    cli_args.extend(DEFAULT_ARGS.split())
    return run_cli(cli_args)

if __name__ == '__main__':
    sys.exit(main(sys.argv[1:]))